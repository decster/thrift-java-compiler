namespace java com.example.generated.dummy

struct MyData {
  1: i32 value
  2: optional string text
}

service Calculator {
  i32 add(1:i32 num1, 2:i32 num2)
  MyData getData(1:string key)
}