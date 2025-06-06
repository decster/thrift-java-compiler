namespace java com.example.thrift

const i64 constMyI64 = 1245738738;
const string constMyString = "Hello, world!";
const double constMyDouble = 3.14159;
const list<i64> constMyList = [1, 2, 3, 4, 5];
const map<string, i64> constMyMap = {"key1": 1, "key2": 2, "key3": 3};
// nested const
const map<string, list<i64>> constMyNestedMap = {
  "nestedKey1": [1, 2, 3],
  "nestedKey2": [4, 5, 6]
};