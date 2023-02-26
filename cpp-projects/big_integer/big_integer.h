#pragma once

#include <iosfwd>
#include <string>
#include <vector>

enum BIT_OP{
  AND,
  OR,
  XOR,
};

struct big_integer {
  big_integer();
  big_integer(big_integer const& other);
  big_integer(short a);
  big_integer(unsigned short a);
  big_integer(int a);
  big_integer(unsigned int a);
  big_integer(long a);
  big_integer(unsigned long a);
  big_integer(long long a);
  big_integer(unsigned long long a);
  explicit big_integer(std::string const& str);
  ~big_integer();

  big_integer& operator=(big_integer const& other);

  big_integer& operator+=(big_integer const& rhs);
  big_integer& operator-=(big_integer const& rhs);
  big_integer& operator*=(big_integer const& rhs);
  big_integer& operator/=(big_integer const& rhs);
  big_integer& operator%=(big_integer const& rhs);

  big_integer& operator&=(big_integer const& rhs);
  big_integer& operator|=(big_integer const& rhs);
  big_integer& operator^=(big_integer const& rhs);

  big_integer& operator<<=(int rhs);
  big_integer& operator>>=(int rhs);

  big_integer operator+() const;
  big_integer operator-() const;
  big_integer operator~() const;

  big_integer& operator++();
  big_integer operator++(int);

  big_integer& operator--();
  big_integer operator--(int);

  friend bool operator==(big_integer const& a, big_integer const& b);
  friend bool operator!=(big_integer const& a, big_integer const& b);
  friend bool operator<(big_integer const& a, big_integer const& b);
  friend bool operator>(big_integer const& a, big_integer const& b);
  friend bool operator<=(big_integer const& a, big_integer const& b);
  friend bool operator>=(big_integer const& a, big_integer const& b);

  friend std::string to_string(big_integer const& a);

  void swap(big_integer& a, big_integer& b);

private:
  std::vector<int32_t> data;
  bool sign;
  int64_t div_by_short(std::vector<int32_t>& a, int64_t rdx, int64_t div);
  void sum (big_integer& a, big_integer const& b);
  void sub (big_integer& a, big_integer const& b);
  big_integer mul_with_short(big_integer& a, int64_t b);
  std::pair<big_integer, big_integer> division(big_integer& divisible, big_integer& divider);
  void align(big_integer& a, big_integer& b);
  void negative_binary(big_integer& a);
  void to_initial_form(big_integer& a);
  void trim(big_integer& a);
  int32_t mod_of_short(big_integer const& a, int32_t num);
  void bit_operation(big_integer& a, big_integer const& b, BIT_OP op);
  int32_t bit_func(int32_t a, int32_t b, BIT_OP op);
  void add_with_short(big_integer& a, int64_t b) const;
  void change_signum(big_integer& a) const;
};

big_integer operator+(big_integer a, big_integer const& b);
big_integer operator-(big_integer a, big_integer const& b);
big_integer operator*(big_integer a, big_integer const& b);
big_integer operator/(big_integer a, big_integer const& b);
big_integer operator%(big_integer a, big_integer const& b);

big_integer operator&(big_integer a, big_integer const& b);
big_integer operator|(big_integer a, big_integer const& b);
big_integer operator^(big_integer a, big_integer const& b);

big_integer operator<<(big_integer a, int b);
big_integer operator>>(big_integer a, int b);

bool operator==(big_integer const& a, big_integer const& b);
bool operator!=(big_integer const& a, big_integer const& b);
bool operator<(big_integer const& a, big_integer const& b);
bool operator>(big_integer const& a, big_integer const& b);
bool operator<=(big_integer const& a, big_integer const& b);
bool operator>=(big_integer const& a, big_integer const& b);

std::string to_string(big_integer const& a);
std::ostream& operator<<(std::ostream& s, big_integer const& a);
