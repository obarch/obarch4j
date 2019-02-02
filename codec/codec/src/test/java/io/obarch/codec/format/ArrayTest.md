# typed_array

| value | encoded |
| ---   | ---     |
| `new String[]{"hello"}` | `["hello"]` |
| `new String[]{"a","b"}` | `["a","b"]` |
| `new String[]{"a","b","c","d","e"}` | `["a","b","c","d","e"]` |
| `new String[]{null}` | `[null]` |
| `new byte[][]{new byte[]{1,2,3}}` | `["\\AB\\AC\\AD"]` |
| `new byte[][]{null}` | `[null]` |
| `new Byte[][]{null}` | `[null]` |
| `new boolean[]{true}` | `[true]` |
| `new Boolean[]{true}` | `[true]` |
| `new byte[]{1,2,3}` | `"\\AB\\AC\\AD"` |
| `new Byte[]{1,2,3}` | `"\\AB\\AC\\AD"` |
| `new short[]{1}` | `["\b;;;;;;<"]` |
| `new Short[]{1}` | `["\b;;;;;;<"]` |
| `new Short[]{null}` | `[null]` |
| `new int[]{1}` | `["\b;;;;;;<"]` |
| `new Integer[]{1}` | `["\b;;;;;;<"]` |
| `new Integer[]{null}` | `[null]` |
| `new long[]{100L}` | `["\b;;;;;;;;;;;;>?"]` |
| `new Long[]{100L}` | `["\b;;;;;;;;;;;;>?"]` |
| `new Long[]{null}` | `[null]` |
| `new float[]{1.1F}` | `["\f;>ZWGTNAK;;;;;"]` |
| `new Float[]{1.1F}` | `["\f;>ZWGTNAK;;;;;"]` |
| `new Float[]{null}` | `[null]` |
| `new double[]{1.1D}` | `["\f;>ZWGTNAGTNAGU"]` |
| `new Double[]{1.1D}` | `["\f;>ZWGTNAGTNAGU"]` |
| `new Double[]{null}` | `[null]` |

# object_array

| value | encoded |
| ---   | ---     |
| `new Object[0]` | `[]` |
| `new Object[1]` | `[null]` |
| `new Object[]{"hello"}` | `["hello"]` |
| `new Object[]{"hello","world"}` | `["hello","world"]` |
| `new Object[]{100L}` | `["\b;;;;;;;;;;;;>?"]` |
| `new Object[]{true}` | `[true]` |
| `new Object[]{1.1D}` | `["\f;>ZWGTNAGTNAGU"]` |
