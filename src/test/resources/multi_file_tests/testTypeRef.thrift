namespace java com.github.decster.gentest

struct MyType {
  1: i32 id
}

struct User {
  1: MyType userType
}

service TestService {
  User getUser(1: MyType userType);
}
