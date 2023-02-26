#include "big_integer.h"
#include <cstddef>
#include <cstring>
#include <ostream>
#include <stdexcept>
#include <cmath>
#include <string>
#include <algorithm>



/*
 * MODEL: big_integer -> data + sign
 * data === vector<int32_t> : higher index - higher digit (value)
 */
static const int64_t RADIX = (1ll << 31);

big_integer::big_integer() : sign(false){
}

big_integer::big_integer(big_integer const& other) = default;

big_integer::big_integer(short a) : big_integer((long long) a) {}

big_integer::big_integer(unsigned short a) : big_integer((long long) a) {}

big_integer::big_integer(int a) : big_integer((long long) a) {}

big_integer::big_integer(unsigned int a) : big_integer((long long) a) {}

big_integer::big_integer(long a) : big_integer((long long) a) {}

big_integer::big_integer(unsigned long a) : big_integer((unsigned long long) a) {}

big_integer::big_integer(long long a) : data() {
  sign = a < 0;
  while (a) {
    data.push_back(std::abs(a % RADIX));
    a /= RADIX;
  }
}

big_integer::big_integer(unsigned long long a) : data() {
  sign = 0;
  while (a) {
    data.push_back(a % RADIX);
    a /= RADIX;
  }
}

big_integer::big_integer(std::string const& str) : sign(false) {
  if (str == "" || str == "-") {
    throw std::invalid_argument("");
  }
  size_t start = 0;
  big_integer res;
  if (str[0] == '-') {
    start = 1;
  }
  int32_t k = 0, pw = 1;
  for (size_t i = start; i < str.length(); i++) {
    if (!(str[i] >= '0' && str[i] <= '9')) {
      throw std::invalid_argument("");
    }
    k = k * 10 + (str[i] - '0');
    pw *= 10;
    if (pw == 1e9) {
      res = mul_with_short(res, pw);
      add_with_short(res, k);
      k = 0;
      pw = 1;
    }
  }
  if (pw > 1) {
    res = mul_with_short(res, pw);
    add_with_short(res, k);
  }
  res.sign = (str[0] == '-');
  swap(*this, res);
}

void big_integer::add_with_short(big_integer& a, int64_t b) const {
  if (a.sign) {
    b = -b;
  }
  for (size_t i = 0; i < a.data.size(); i++) {
    b += static_cast<int64_t> (a.data[i]);
    a.data[i] = b % RADIX;
    b /= RADIX;
    if (a.data[i] < 0) {
      b = -1;
      a.data[i] += RADIX;
    }
  }
  if (b > 0) {
    a.data.push_back(b);
  } else if (b < 0) {
    a.data.push_back(-b);
    a.sign = !a.sign;
  }
}

int64_t big_integer::div_by_short(std::vector<int32_t>& a, int64_t rdx, int64_t div) {
  int64_t r = 0;
  std::vector<int32_t> res;
  bool was_found = false;
  for (size_t i = a.size(); i > 0; i--) {
    r = r * rdx + a[i - 1];
    if ((r >= div && !was_found) || was_found) {
      res.push_back(r / div);
      r %= div;
      was_found = true;
    }
  }
  std::reverse(res.begin(), res.end());
  a.swap(res);
  return r;
}

big_integer::~big_integer() {}

big_integer& big_integer::operator=(big_integer const& other) = default;

void big_integer::swap(big_integer& a, big_integer& b) {
  a.data.swap(b.data);
  std::swap(a.sign, b.sign);
}

big_integer& big_integer::operator+=(big_integer const& rhs) {
  if (this->sign < rhs.sign) {
    sub(*this, rhs);
  } else if (this->sign > rhs.sign) {
    big_integer other(rhs);
    sub(other, *this);
    swap(*this, other);
  } else {
    sum(*this, rhs);
  }
  return *this;
}

void big_integer::sum(big_integer& a, const big_integer& b) {
  big_integer other(b);
  align(a, other);
  int64_t r = 0;
  for (size_t i = 0; i < a.data.size(); i++) {
    r += static_cast<int64_t> (a.data[i]) + static_cast<int64_t> (other.data[i]);
    a.data[i] = r % RADIX;
    r /= RADIX;
  }
  a.data.push_back(r);
  trim(a);
}

void big_integer::sub(big_integer& a, const big_integer& b) {
  big_integer other(b);
  bool res_sign = a.sign;
  a.sign = 0;
  other.sign = 0;
  if (a < other) {
    swap(a, other);
    res_sign = !res_sign;
  }
  align(a, other);
  int32_t r = 0;
  for (size_t i = 0; i < a.data.size(); i++) {
    int64_t q = static_cast<int64_t> (a.data[i]) + r + RADIX - static_cast<int64_t> (other.data[i]);
    if (q >= RADIX) {
      a.data[i] = q % RADIX;
      r = 0;
    } else {
      a.data[i] = q;
      r = -1;
    }
  }
  a.sign = res_sign;
  trim(a);
}

big_integer& big_integer::operator-=(big_integer const& rhs) {
  if (rhs.sign != this->sign) {
    sum(*this, rhs);
    if (rhs.sign) {
      this->sign = 0;
    }
    return *this;
  }
  sub(*this, rhs);
  return *this;
}

big_integer& big_integer::operator*=(big_integer const& other) {
  big_integer result;
  bool sgn = this->sign ^ other.sign;
  this->sign = 0;
  for (size_t i = 0; i < other.data.size(); i++) {
    big_integer q(*this);
    int32_t b = other.data[i];
    if (b < 0) {
      q = -q;
      b = -b;
    }
    int64_t r = 0;
    for (size_t i = 0; i < q.data.size(); i++) {
      r += static_cast<int64_t> (q.data[i]) * b;
      q.data[i] = r % RADIX;
      r /= RADIX;
    }
    while (r) {
      q.data.push_back(r % RADIX);
      r /= RADIX;
    }
    q <<= 31 * i;
    result += q;
  }
  result.sign = sgn;
  swap(*this, result);
  trim(*this);
  return *this;
}

big_integer big_integer::mul_with_short(big_integer& a, int64_t b) {
  int64_t r = 0;
  big_integer res(a);
  if (b < 0) {
    res = -res;
    b = -b;
  }
  for (size_t i = 0; i < res.data.size(); i++) {
    r += static_cast<int64_t> (res.data[i]) * b;
    res.data[i] = r % RADIX;
    r /= RADIX;
  }
  while (r) {
    res.data.push_back(r % RADIX);
    r /= RADIX;
  }
  return res;
}

std::pair<big_integer, big_integer> big_integer::division(big_integer& divisible, big_integer& divider) {
  big_integer res;
  if (divisible.data.size() < divider.data.size()) {
    return {res, divisible};
  }
  bool divisible_sign = divisible.sign, divider_sign = divider.sign;
  divisible.sign = 0;
  divider.sign = 0;
  int64_t k = RADIX / (divider.data[divider.data.size() - 1] + 1);
  divider = mul_with_short(divider, k);
  divisible = mul_with_short(divisible, k);
  size_t m = divisible.data.size() - divider.data.size(), n = divider.data.size();
  res.data.resize(m + 1);
  if (divisible >= (divider << 31 * m)) {
    res.data[m] = 1;
    divisible -= (divider << 31 * m);
    if (divisible.data.size() == 0) {
      divisible.data.push_back(0);
      divisible.sign = 0;
    }
    if (divisible.data.size() < divider.data.size()) {
      m = 0;
    } else {
      m = divisible.data.size() - divider.data.size();
    }
  } else {
    res.data[m] = 0;
  }
  int64_t a, b, q, r, p;
  big_integer shifted;
  for (size_t j = m; j > 0; j--) {
    a = n + j - 1 < divisible.data.size() ? divisible.data[n + j - 1] : 0;
    b = n + j - 2 < divisible.data.size() ? divisible.data[n + j - 2] : 0;
    q = (a * RADIX + b) / divider.data[n - 1];
    r = (a * RADIX + b) % divider.data[n - 1];
    if (j + n - 3 < divisible.data.size()) {
      p = divisible.data[j + n - 3];
    } else {
      p = 0;
    }
    while (q == RADIX ||(n >= 2 && j + n >= 3 && q * divider.data[n - 2] > RADIX * r + p)) {
      q--;
      r += divider.data[n - 1];
      if (r >= RADIX) {
        break;
      }
    }
    res.data[j - 1] = std::min(q, RADIX - 1);
    shifted = divider;
    shifted <<= 31 * (j - 1);
    divisible -= mul_with_short(shifted, res.data[j - 1]);
    while (divisible.sign) {
      res.data[j - 1]--;
      divisible += shifted;
    }
  }
  trim(res);
  res.sign = divider_sign ^ divisible_sign;
  div_by_short(divisible.data, RADIX, k);
  trim(divisible);
  divisible.sign = divisible_sign;
  return {res, divisible};
}

big_integer& big_integer::operator/=(big_integer const& other) {
  big_integer dv(other);
  std::pair<big_integer, big_integer> p = division(*this, dv);
  swap(*this, p.first);
  return *this;
}

big_integer& big_integer::operator%=(big_integer const& other) {
  big_integer dv(other);
  std::pair<big_integer, big_integer> p = division(*this, dv);
  swap(*this, p.second);
  return *this;
}

void big_integer::align(big_integer& a, big_integer& b) {
  size_t sz = std::max(a.data.size(), b.data.size());
  a.data.resize(sz);
  b.data.resize(sz);
}

void big_integer::negative_binary(big_integer& a) {
  if (a.sign) {
    ++a;
    for (size_t i = 0; i < a.data.size(); i++) {
      a.data[i] = ~a.data[i];
    }
  }
}

void big_integer::trim(big_integer& a) {
  while (a.data.size() && a.data[a.data.size() - 1] == 0) {
    a.data.pop_back();
  }
  if (a.data.size() == 0) {
    a.sign = 0;
  }
}

void big_integer::to_initial_form(big_integer& a) {
  a.sign = a.data[a.data.size() - 1] < 0;
  if (a.sign) {
    for (size_t i = 0; i < a.data.size(); i++) {
      a.data[i] = ~a.data[i];
    }
    --a;
  }
}

void big_integer::bit_operation(big_integer& a, big_integer const& b, BIT_OP op) {
  big_integer copy(b);
  align(a, copy);
  negative_binary(a);
  negative_binary(copy);

  int32_t r1 = a.sign ? -1 : 0, r2 = copy.sign ? -1 : 0;
  size_t sz = std::max(a.data.size(), copy.data.size());

  for (size_t i = 0; i < a.data.size(); i++) {
    a.data[i] = bit_func(a.data[i], i >= copy.data.size() ? r2 : copy.data[i], op);
  }
  while (a.data.size() < sz) {
    a.data.push_back(bit_func(r1, copy.data[a.data.size()], op));
  }
  to_initial_form(a);
  trim(a);
}

int32_t big_integer::bit_func(int32_t a, int32_t b, BIT_OP op) {
  switch (op) {
    case AND: return (a & b);
    case OR: return (a | b);
    case XOR: return (a ^ b);
  }
}

big_integer& big_integer::operator&=(big_integer const& other) {
  bit_operation(*this, other, AND);
  return *this;
}

big_integer& big_integer::operator|=(big_integer const& other) {
  bit_operation(*this, other, OR);
  return *this;
}

big_integer& big_integer::operator^=(big_integer const& other) {
  bit_operation(*this, other, XOR);
  return *this;
}

big_integer& big_integer::operator<<=(int num) {
  *this = mul_with_short(*this,  1ll << num % 31);
  size_t sz = num / 31;
  this->data.resize(this->data.size() + sz);
  for (size_t i = this->data.size(); i > sz; i--) {
    this->data[i - 1] = this->data[i - sz - 1];
  }
  for (size_t i = 0; i < sz; i++) {
    this->data[i] = 0;
  }
  return *this;
}

big_integer& big_integer::operator>>=(int num) {
  data.erase(data.begin(), data.begin() + num / 31);
  if (data.size() == 0) {
    data.push_back(0);
    sign = 0;
  }
  int64_t pw = pow(2, num % 31);
  div_by_short(this->data, RADIX, pw);
  trim(*this);
  if (this->sign) {
    *this -= 1;
  }
  return *this;
}

int32_t big_integer::mod_of_short(big_integer const& a, int32_t num) {
  int64_t r = 0;
  int64_t rad = 1;
  for (size_t i = 0; i < a.data.size(); i++) {
    r = (r + a.data[i] * rad) % num;
    rad = (rad * RADIX) % num;
  }
  return r;
}

big_integer big_integer::operator+() const {
  return *this;
}

big_integer big_integer::operator-() const {
  big_integer res(*this);
  change_signum(res);
  return res;
}

void big_integer::change_signum(big_integer& a) const {
  a.sign = !a.sign;
}

big_integer big_integer::operator~() const {
  big_integer copy(*this);
  change_signum(copy);
  add_with_short(copy, -1);
  return copy;
}

big_integer& big_integer::operator++() {
  add_with_short(*this, 1);
  return *this;
}

big_integer big_integer::operator++(int) {
  big_integer copy(*this);
  add_with_short(*this, 1);
  return copy;
}

big_integer& big_integer::operator--() {
  add_with_short(*this, -1);
  return *this;
}

big_integer big_integer::operator--(int) {
  big_integer copy(*this);
  add_with_short(*this, -1);
  return copy;
}

big_integer operator+(big_integer a, big_integer const& b) {
  return a += b;
}

big_integer operator-(big_integer a, big_integer const& b) {
  return a -= b;
}

big_integer operator*(big_integer a, big_integer const& b) {
  return a *= b;
}

big_integer operator/(big_integer a, big_integer const& b) {
  return a /= b;
}

big_integer operator%(big_integer a, big_integer const& b) {
  return a %= b;
}

big_integer operator&(big_integer a, big_integer const& b) {
  return a &= b;
}

big_integer operator|(big_integer a, big_integer const& b) {
  return a |= b;
}

big_integer operator^(big_integer a, big_integer const& b) {
  return a ^= b;
}

big_integer operator<<(big_integer a, int b) {
  return a <<= b;
}

big_integer operator>>(big_integer a, int b) {
  return a >>= b;
}

bool operator==(big_integer const& a, big_integer const& b) {
  if (!a.data.size() && !b.data.size()) {
    return true;
  }
  if (a.sign != b.sign) {
    return false;
  }
  return a.data == b.data;
}

bool operator!=(big_integer const& a, big_integer const& b) {
  return !(a == b);
}

bool operator<(big_integer const& a, big_integer const& b) {
  if (a.sign > b.sign) {
    return true;
  }
  if (a.sign < b.sign) {
    return false;
  }
  bool res = 1;
  if (a.data.size() > b.data.size()) {
    res = 0;
  } else if (a.data.size() == b.data.size()) {
    bool eq = 1;
    for (size_t i = a.data.size(); i > 0; i--) {
      if (a.data[i - 1] > b.data[i - 1]) {
          res = 0;
          break;
      }
      eq &= (a.data[i - 1] == b.data[i - 1]);
      if (!eq) {
        break;
      }
    }
    if (eq) {
      res = 0;
    }
  }
  return res ^ a.sign;
}

bool operator>(big_integer const& a, big_integer const& b) {
  return b < a;
}

bool operator<=(big_integer const& a, big_integer const& b) {
  return !(b < a);
}

bool operator>=(big_integer const& a, big_integer const& b) {
  return !(a < b);
}

std::string to_string(big_integer const& a) {
  if (a == 0) {
    return "0";
  }
  big_integer copy(a);
  std::string str = "";
  while (true) {
    std::string md = std::to_string(std::abs(copy.mod_of_short(copy, 1e9)));
    std::reverse(md.begin(), md.end());
    copy.div_by_short(copy.data, RADIX, 1e9);
    copy.trim(copy);
    if (copy == 0) {
      str += md;
      break;
    }
    while (md.length() < 9) {
      md += '0';
    }
    str += md;
  }
  if (a.sign) {
    str += "-";
  }
  std::reverse(str.begin(), str.end());
  return str;
}

std::ostream& operator<<(std::ostream& s, big_integer const& a) {
  return s << to_string(a);
}
