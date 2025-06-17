namespace java com.example.thrift

service DemoService {
  void demoMethod(1: string arg1, 2: string arg2, 3: i32 arg3)
  string demoMethod2(1: i32 id, 2: string name)
  map<i32, string> demoMethod3(1: list<i32> ids)
  bool demoMethod4(/** demoMethodData comment */1: binary data, 2: i64 timestamp)
}
