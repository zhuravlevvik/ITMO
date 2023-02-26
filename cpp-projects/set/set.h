#include <cassert>  // assert
#include <iterator> // std::reverse_iterator
#include <utility>  // std::pair, std::swap

template <typename T>
struct set {

  struct base_node {
    base_node() : left_(nullptr), right_(nullptr), par_(nullptr) {}

    base_node(base_node const& other) = default;

    base_node(base_node* l, base_node* r, base_node* p) : left_(l), right_(r), par_(p) {}

    ~base_node() = default;

    base_node* next() const {
      if (right_) {
        return find_left(right_);
      }

      base_node* p = par_;
      base_node* v = const_cast<base_node*>(this);

      while (p != p->par_ && p->right_ == v) {
        v = p;
        p = p->par_;
      }
      return p;
    }

    base_node* next() {
      if (right_) {
        return find_left(right_);
      }

      base_node* p = par_;
      base_node* v = this;

      while (p != p->par_ && p->right_ == v) {
        v = p;
        p = p->par_;
      }
      return p;
    }

    friend base_node* find_left(base_node* node) {
      while (node->left_) {
        node = node->left_;
      }
      return node;
    }

    base_node* prev() const {
      if (left_) {
        return find_right(left_);
      }

      base_node* p = par_;
      base_node* v = const_cast<base_node*>(this);
      while (p->left_ == v) {
        v = p;
        p = p->par_;
      }
      return p;
    }

    base_node* prev() {
      if (left_) {
        return find_right(left_);
      }

      base_node* p = par_;
      base_node* v = this;
      while (p->left_ == v) {
        v = p;
        p = p->par_;
      }
      return p;
    }

    friend base_node* find_right(base_node* node) {
      while (node->right_) {
        node = node->right_;
      }
      return node;
    }

    base_node* left_;
    base_node* right_;
    base_node* par_;
  };

  struct node : base_node {
    node(T const& val) : base_node(), value_(val) {}

    ~node() = default;

    T value_;
  };

  struct iterator {
    friend struct base_node;
    friend struct set;

    using iterator_category = std::bidirectional_iterator_tag;
    using difference_type = std::ptrdiff_t;
    using value_type = T const;
    using pointer = T const*;
    using reference = T const&;

    iterator() = default;

    iterator(iterator const& other) = default;

    iterator& operator=(iterator const& other) = default;

    void swap(iterator& other) {
      std::swap(nd, other.nd);
    }

    reference operator*() const {
      return static_cast<node const*>(nd)->value_;
    } // O(1) nothrow

    pointer operator->() const {
      return &static_cast<node const*>(nd)->value_;
    }  // O(1) nothrow

    iterator& operator++() & {
      nd = nd->next();
      return *this;
    }   //      nothrow

    iterator operator++(int) & {
      iterator copy(*this);
      operator++();
      return copy;
    }//      nothrow

    iterator& operator--() & {
      nd = nd->prev();
      return *this;
    }   //      nothrow

    iterator operator--(int) & {
      iterator copy(*this);
      operator--();
      return copy;
    } //      nothrow

    friend bool operator==(iterator const& a, iterator const& b) {
      return a.nd == b.nd;
    }

    friend bool operator!=(iterator const& a, iterator const& b) {
      return !(a == b);
    }

  private:
    base_node const* nd;

    iterator(base_node const* nd) : nd(nd) {}
  };

  friend struct node;
  friend struct iterator;

  using const_iterator = iterator;
  using reverse_iterator = std::reverse_iterator<iterator>;
  using const_reverse_iterator = std::reverse_iterator<const_iterator>;

  set() : root(nullptr, nullptr, &root) {}                            // O(1) nothrow

  set(set const& other) : set() {
    for (const auto& i : other) {
      try {
        insert(i);
      } catch (...) {
        clear();
        throw;
      }
    }
  }            // O(n) strong

  set& operator=(set const& other) {
    if (this == &other) {
      return *this;
    }
    set copy(other);
    (*this).swap(copy);
    return *this;
  } // O(n) strong

  ~set() {
      clear();
  };                          // O(n) nothrow

  void clear() {

    while (!empty()) {
      erase(begin());
    }

    root.left_ = root.right_ = nullptr;
  } // O(n) nothrow

  bool empty() {
    return !root.left_;
  } // O(1) nothrow

  bool empty() const {
    return !root.left_;
  }

  const_iterator begin() const {
    if (empty()) {
      return end();
    }
    return iterator(find_left(root.left_));
  } //      nothrow

  const_iterator end() const {
    return iterator(&root);
  }   //      nothrow

  const_reverse_iterator rbegin() const {
    return reverse_iterator(end());
  } //      nothrow

  const_reverse_iterator rend() const {
    return reverse_iterator(begin());
  }   //      nothrow

  std::pair<iterator, bool> insert(T const& val) {
    iterator it = find(val);
    if (it != end()) {
      return {it, false};
    }
    node* nd = new node(val);
    node* x = static_cast<node*>(root.left_);
    while (x) {
      bool cmp;
      try {
        cmp = val > x->value_;
      } catch (...) {
        delete(nd);
        throw;
      }
      if (cmp) {
        if (x->right_) {
          x = static_cast<node*>(x->right_);
        } else {
          nd->par_ = x;
          x->right_ = nd;
          return {iterator(nd), true};
        }
      } else {
        if (x->left_) {
          x = static_cast<node*>(x->left_);
        } else {
          nd->par_ = x;
          x->left_ = nd;
          return {iterator(nd), true};
        }
      }
    }
    root.left_ = nd;
    nd->par_ = &root;
    return {iterator(nd), true};
  } // O(h) strong

  iterator erase(iterator it) {
    if (it == end()) {
      return it;
    }
    node* v = static_cast<node*>(const_cast<base_node*>(it.nd));
    iterator ret = iterator(v->next());
    node* p = static_cast<node*>(v->par_);
    if (!v->left_ && !v->right_) {
      if (p->left_ == v) {
        p->left_ = nullptr;
      } else {
        p->right_ = nullptr;
      }
    } else if (!v->left_) {
      if (p->left_ == v) {
        p->left_ = v->right_;
      } else {
        p->right_ = v->right_;
      }
      v->right_->par_ = p;
      delete(v);
      return ret;
    } else if (!v->right_) {
      if (p->left_ == v) {
        p->left_ = v->left_;
      } else {
        p->right_ = v->left_;
      }
      v->left_->par_ = p;
      delete(v);
      return ret;
    } else {
      node* s = static_cast<node*>(v->next());
      if (p->left_ == v) {
        p->left_ = s;
      } else {
        p->right_ = s;
      }
      if (s->par_->left_ == s) {
        s->par_->left_ = s->right_;
      } else {
        s->par_->right_ = s->right_;
      }
      if (s->right_) {
        s->right_->par_ = s->par_;
      }
      s->left_ = v->left_;
      if (s->left_) {
        s->left_->par_ = s;
      }
      s->right_ = v->right_;
      if (s->right_) {
        s->right_->par_ = s;
      }
      s->par_ = p;
    }
    delete(v);
    return ret;
  }                   // O(h) nothrow

  const_iterator find(T const& val) const {
    node* v = static_cast<node*>(root.left_);
    while (v) {
      if (v->value_ == val) {
        return iterator(v);
      }
      if (v->value_ > val) {
        v = static_cast<node*>(v->left_);
      } else {
        v = static_cast<node*>(v->right_);
      }
    }
    return end();
  }        // O(h) strong

  const_iterator lower_bound(T const& val) const {
    node* v = static_cast<node*>(root.left_);
    node* ot = nullptr;
    while (v) {
      if (v->value_ == val) {
        return iterator(v);
      }
      if (v->value_ < val) {
        v = static_cast<node*>(v->right_);
      } else {
        ot = v;
        v = static_cast<node*>(v->left_);
      }
    }
    if (ot) {
      return iterator(ot);
    }
    return end();
  } // O(h) strong

  const_iterator upper_bound(T const& val) const {
    node* v = static_cast<node*>(root.left_);
    node* ot = nullptr;
    while (v) {
      if (v->value_ <= val) {
        v = static_cast<node*>(v->right_);
      } else {
        ot = v;
        v = static_cast<node*>(v->left_);
      }
    }
    if (ot) {
      return iterator(ot);
    }
    return end();
  } // O(h) strong

  void swap(set& other) {
    base_node* l1 = root.left_;
    base_node* l2 = other.root.left_;
    root = base_node(nullptr, nullptr, &root);
    root.left_ = l2;
    if (l2) {
      l2->par_ = &root;
    }
    other.root = base_node(nullptr, nullptr, &other.root);
    other.root.left_ = l1;
    if (l1) {
      l1->par_ = &other.root;
    }
  } // O(1) nothrow

  friend void swap(set& a, set& b) {
    a.swap(b);
  }

private:
  base_node root;
};
