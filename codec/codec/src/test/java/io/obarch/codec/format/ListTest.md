# typed_list

| type | value | encoded |
| ---  | ---   | ---  |
| `AnyList<String>` | `null` | `null` |
| `AnyList<String>` | `new AnyList()` | `[]` |
| `AnyList<String>` | `new AnyList("hello")` | `["hello"]` |
| `AnyList<String>` | `new AnyList((String)null)` | `[null]` |
| `AnyList<Integer>` | `new AnyList(100)` | `["\b;;;;;>?"]` |

# object_list

| type | value | encoded |
| ---  | ---   | ---  |
| `AnyList` | `null` | `null` |
| `AnyList` | `new AnyList()` | `[]` |
| `AnyList` | `new AnyList("hello")` | `["hello"]` |