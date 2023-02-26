#pragma once
#include <array>
#include <cstddef>

template <typename T, size_t SMALL_SIZE>
struct socow_vector {
  using iterator = T*;
  using const_iterator = T const*;

  socow_vector() : size_(0), is_small(true) {}

  socow_vector(socow_vector const& other) : socow_vector() {
    if (other.is_small) {
      copy_raw(other.static_.begin(), static_.begin(), other.size_, 0);
      size_ = other.size_;
      is_small = true;
      return;
    }
    is_small = false;
    dynamic_ = other.dynamic_;
    other.add_ref();
    size_ = other.size_;
  }

  socow_vector& operator=(socow_vector const& other) {
    if (this == &other) {
      return *this;
    }
    socow_vector copy(other);
    swap(copy);
    return *this;
  }

  ~socow_vector() {
    if (is_small) {
      destroy_elements(static_.begin(), size_, 0);
      return;
    }
    destroy_dd(dynamic_);
  }

  T& operator[](size_t i) {
    return data()[i];
  }

  T const& operator[](size_t i) const {
    return data()[i];
  }

  T* data() {
    check_cow();
    if (is_small) {
      return static_.begin();
    } else {
      return dynamic_->data;
    }
  }

  T const* data() const {
    if (is_small) {
      return static_.begin();
    } else {
      return dynamic_->data;
    }
  }

  size_t size() const {
    return size_;
  }

  T& front() {
    return data()[0];
  }

  T const& front() const {
    return data()[0];
  }

  T& back() {
    return data()[size_ - 1];
  }

  T const& back() const {
    return data()[size_ - 1];
  }

  void push_back(T const& el) {
    if (!need_copy() && size_ < capacity()) {
      new (data() + size_) T(el);
      size_++;
      return;
    }
    header* tmp;
    if (need_copy() && size_ < capacity()) {
      tmp = create_dd(data(), capacity());
    } else {
      tmp = create_dd(data(), capacity() * 2 + 1);
    }
    try {
      new (tmp->data + size_) T(el);
    } catch (...) {
      destroy_elements(tmp->data, size_, 0);
      operator delete(tmp);
      throw;
    }
    if (is_small) {
      destroy_elements(data(), size_, 0);
    } else {
      destroy_dd(dynamic_);
    }
    size_++;
    dynamic_ = tmp;
    is_small = false;
  }

  void pop_back() {
    data()[--size_].~T();
  }

  bool empty() const {
    return size_ == 0;
  }

  size_t capacity() const {
    if (is_small) {
      return SMALL_SIZE;
    }
    return dynamic_->capacity_;
  }

  void reserve(size_t new_cap) {
    if (new_cap <= capacity() && !need_copy()) {
      return;
    }
    header* tmp = create_dd(data(), std::max(new_cap, size_));
    if (is_small) {
      destroy_elements(data(), size_, 0);
    } else {
      destroy_dd(dynamic_);
    }
    dynamic_ = tmp;
    is_small = false;
  }

  void shrink_to_fit() {
    if (is_small || size_ == capacity()) {
      return;
    }
    if (size_ <= SMALL_SIZE) {
      header* tmp = dynamic_;
      try {
        copy_raw(tmp->data, static_.begin(), size_, 0);
      } catch (...) {
        dynamic_ = tmp;
        throw;
      }
      is_small = true;
      destroy_dd(tmp);
      return;
    }
    check_cow();
    dynamic_->capacity_ = size_;
  }

  void clear() {
    if (is_small) {
      destroy_elements(static_.begin(), size_, 0);
    } else {
      if (dynamic_->ref_count_ == 1) {
        destroy_elements(dynamic_->data, size_, 0);
      } else {
        header* tmp = create_new_header(capacity());
        destroy_dd(dynamic_);
        dynamic_ = tmp;
      }
    }
    size_ = 0;
  }

  void swap(socow_vector& other) {
    if (is_small && other.is_small) {
      if (other.size_ >= size_) {
        small_small_swap(other, *this);
      } else {
        small_small_swap(*this, other);
      }
    } else if (is_small && !other.is_small) {
      small_big_swap(*this, other);
    } else if (!is_small && other.is_small) {
      small_big_swap(other, *this);
    } else {
      std::swap(dynamic_, other.dynamic_);
    }
    std::swap(size_, other.size_);
  }

  iterator begin() {
    return data();
  }

  const_iterator begin() const {
    return data();
  }

  iterator end() {
    return begin() + size_;
  }

  const_iterator end() const {
    return begin() + size_;
  }

  iterator insert(const_iterator pos, T const& el) {
    size_t ind = pos - std::as_const(*this).begin();
    push_back(el);
    for (size_t i = ind; i < size_ - 1; i++) {
      std::swap(data()[i], data()[size_ - 1]);
    }
    return begin() + ind;
  }

  iterator erase(const_iterator pos) {
    return erase(pos, pos + 1);
  }

  iterator erase(const_iterator first, const_iterator last) {
    size_t left_ind = first - std::as_const(*this).begin();
    size_t right_ind = last - std::as_const(*this).begin();
    size_t len = right_ind - left_ind;
    if (len == 0) {
      return const_cast<iterator>(last);
    }
    check_cow();
    for (size_t i = left_ind; i < size_ - len; i++) {
      std::swap(data()[i], data()[i + len]);
    }
    for (size_t i = 0; i < len; i++) {
      pop_back();
    }
    return begin() + left_ind;
  }

private:
  struct header{
    size_t capacity_;
    size_t ref_count_;
    T data[0];
  };
  union {
    header* dynamic_;
    std::array<T, SMALL_SIZE> static_;
  };
  size_t size_;
  bool is_small;

  header* create_dd (T const* src, size_t new_size) {
    header* new_data = create_new_header(new_size);
    try {
      copy_raw(src, new_data->data, size_, 0);
    } catch (...) {
      operator delete(new_data);
      throw;
    }
    return new_data;
  }

  header* create_new_header(size_t new_size) {
    header* new_data = new((operator new(sizeof(header) + new_size * sizeof(T)))) header{new_size, 1};
    return new_data;
  }

  void check_cow() {
    if (is_small) {
      return;
    }
    if (dynamic_->ref_count_ > 1) {
      header* tmp = create_dd(dynamic_->data,capacity());
      destroy_dd(dynamic_);
      dynamic_ = tmp;
    }
  }

  void add_ref() const {
    dynamic_->ref_count_++;
  }

  void destroy_elements(T* data, size_t size, size_t start) {
    for (size_t i = size; i > start; i--) {
      data[i - 1].~T();
    }
  }

  void small_big_swap(socow_vector& a, socow_vector& b) {
    header* copy = b.dynamic_;
    b.dynamic_ = nullptr;
    try {
      copy_raw(a.static_.begin(), b.static_.begin(), a.size_, 0);
    } catch (...) {
      b.dynamic_ = copy;
      throw;
    }
    destroy_elements(a.static_.begin(), a.size_, 0);
    a.dynamic_ = copy;
    a.is_small = false;
    b.is_small = true;
  }

  bool need_copy() {
    return !is_small && dynamic_->ref_count_ > 1;
  }

  void destroy_dd(header* data) {
    if (data->ref_count_ > 1) {
      data->ref_count_--;
    } else {
      destroy_elements(data->data, size_, 0);
      operator delete(data);
    }
  }

  void copy_raw(T const* src, T* dst, size_t size, size_t start) {
    for (size_t i = start; i < size; i++) {
      try {
        new (dst + i) T(src[i]);
      } catch (...) {
        destroy_elements(dst, i, start);
        throw;
      }
    }
  }

  void small_small_swap(socow_vector& a, socow_vector& b) {
    copy_raw(a.data(), b.data(), a.size_, b.size_);
    for (size_t i = a.size_; i > b.size_; i--) {
      a[i - 1].~T();
    }
    for (size_t i = 0; i < b.size_; i++) {
      std::swap(a[i], b[i]);
    }
  }
};
