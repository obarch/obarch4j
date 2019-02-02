# typed_map

| type | value | encoded |
| ---  | ---   | ---     |
| `AnyMap<String,String>` | `null` | `null` |
| `AnyMap<String,String>` | `new AnyMap("a","b")` | `{"a":"b"}` |
| `AnyMap<String,String>` | `new AnyMap("a",null)` | `{"a":null}` |
| `AnyMap<String,Integer>` | `new AnyMap("a",100)` | `{"a":"\b;;;;;>?"}` |
| `AnyMap<Integer,String>` | `new AnyMap(100,"a")` | `{"\b;;;;;>?":"a"}` |
| `AnyMap<Boolean,String>` | `new AnyMap(true,"a")` | `{"true":"a"}` |
| `AnyMap<AnyList<Boolean>,String>` | `new AnyMap(new AnyList(true),"a")` | `{"[true]":"a"}` |

# null_key

| value | encoded |
| ---   | ---     |
| `new AnyMap(null,"b")` | `{"null":"b"}` |

