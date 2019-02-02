# java_util_Date

| type | value | encoded |
| ---  | ---   | ---  |
| `java.util.Date` | `null` | `null` |
| `java.util.Date` | `new java.util.Date(0)` | `"\b;;;;;;;;;;;;;;"` |
| `java.util.Date` | `new java.util.Date(1024)` | `"\b;;;;;;;;;;;<;;"` |

# java_time_LocalDate

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.LocalDate` | `null` | `null` |
| `java.time.LocalDate` | `java.time.LocalDate.of(2008,8,8)` | `["\b;;;;<YS","\b;;;;;;C","\b;;;;;;C"]` |
| `java.time.LocalDate` | `java.time.LocalDate.of(1985,6,22)` | `["\b;;;;<Y<","\b;;;;;;A","\b;;;;;;Q"]` |

# java_time_LocalDateTime

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.LocalDateTime` | `null` | `null` |
| `java.time.LocalDateTime` | `java.time.LocalDateTime.of(2008,8,8,1,2,3,4)` | `["\b;;;;<YS","\b;;;;;;C","\b;;;;;;C","\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;?"]` |
| `java.time.LocalDateTime` | `java.time.LocalDateTime.of(2008,8,8,1,2,3)` | `["\b;;;;<YS","\b;;;;;;C","\b;;;;;;C","\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;;"]` |

# java_time_LocalTime

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.LocalTime` | `null` | `null` |
| `java.time.LocalTime` | `java.time.LocalTime.of(1,2,3,4)` | `["\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;?"]` |

# java_time_MonthDay

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.MonthDay` | `null` | `null` |
| `java.time.MonthDay` | `java.time.MonthDay.of(1,2)` | `["\b;;;;;;<","\b;;;;;;="]` |

# java_time_OffsetDateTime

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.OffsetDateTime` | `null` | `null` |
| `java.time.OffsetDateTime` | `java.time.OffsetDateTime.of(2008,8,8,1,2,3,4,java.time.ZoneOffset.UTC)` | `["\b;;;;<YS","\b;;;;;;C","\b;;;;;;C","\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;?","\b;;;;;;;"]` |

# java_time_OffsetTime

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.OffsetTime` | `null` | `null` |
| `java.time.OffsetTime` | `java.time.OffsetTime.of(1, 2, 3, 4, java.time.ZoneOffset.UTC)` | `["\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;?","\b;;;;;;;"]` |

# java_time_YearMonth

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.YearMonth` | `null` | `null` |
| `java.time.YearMonth` | `java.time.YearMonth.of(2018, 8)` | `["\b;;;;<Z=","\b;;;;;;C"]` |

# java_time_Year

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.Year` | `null` | `null` |
| `java.time.Year` | `java.time.Year.of(2018)` | `"\b;;;;<Z="` |

# java_time_ZonedDateTime

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.ZonedDateTime` | `null` | `null` |
| `java.time.ZonedDateTime` | `java.time.ZonedDateTime.of(2008,8,8,1,2,3,4,java.time.ZoneId.of("UTC"))` | `[["\b;;;;<YS","\b;;;;;;C","\b;;;;;;C","\b;;;;;;<","\b;;;;;;=","\b;;;;;;>","\b;;;;;;?","\b;;;;;;;"],"UTC"]` |

# java_time_Instant

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.Instant` | `null` | `null` |
| `java.time.Instant` | `java.time.Instant.ofEpochSecond(1, 2)` | `["\b;;;;;;;;;;;;;<","\b;;;;;;="]` |

# java_time_Duration

| type | value | encoded |
| ---  | ---   | ---  |
| `java.time.Duration` | `null` | `null` |
| `java.time.Duration` | `java.time.Duration.ofSeconds(1, 2)` | `["\b;;;;;;;;;;;;;<","\b;;;;;;="]` |