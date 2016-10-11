# EnumSet Benchmark

#### EnumSet

[EnumSet](./EnumSet.md)

#### Benchmark

两个Enum，一个26个元素，一个130个元素。

分别循环1000000, 10000000, 80000000进行`contains()`,`add()`,`remove()`操作。

对比EnumSet和传统的Set数据结构的性能：

测试结果：

```
Enum Set 	 26-elements 	 loop 1000000 	 [ contains ]:		 212
Gene Set 	 26-elements 	 loop 1000000 	 [ contains ]:		 146

Enum Set 	 26-elements 	 loop 10000000 	 [ contains ]:		 1089
Gene Set 	 26-elements 	 loop 10000000 	 [ contains ]:		 1074

Enum Set 	 26-elements 	 loop 80000000 	 [ contains ]:		 3444
Gene Set 	 26-elements 	 loop 80000000 	 [ contains ]:		 3533

Enum Set 	 130-elements 	 loop 1000000 	 [ contains ]:		 119
Gene Set 	 130-elements 	 loop 1000000 	 [ contains ]:		 94

Enum Set 	 130-elements 	 loop 10000000 	 [ contains ]:		 799
Gene Set 	 130-elements 	 loop 10000000 	 [ contains ]:		 867

Enum Set 	 130-elements 	 loop 80000000 	 [ contains ]:		 6202
Gene Set 	 130-elements 	 loop 80000000 	 [ contains ]:		 7172

Enum Set 	 26-elements 	 loop 1000000 	 [ add ]:		 87
Gene Set 	 26-elements 	 loop 1000000 	 [ add ]:		 108

Enum Set 	 26-elements 	 loop 10000000 	 [ add ]:		 424
Gene Set 	 26-elements 	 loop 10000000 	 [ add ]:		 570

Enum Set 	 26-elements 	 loop 80000000 	 [ add ]:		 3640
Gene Set 	 26-elements 	 loop 80000000 	 [ add ]:		 4161

Enum Set 	 130-elements 	 loop 1000000 	 [ add ]:		 81
Gene Set 	 130-elements 	 loop 1000000 	 [ add ]:		 89

Enum Set 	 130-elements 	 loop 10000000 	 [ add ]:		 830
Gene Set 	 130-elements 	 loop 10000000 	 [ add ]:		 899

Enum Set 	 130-elements 	 loop 80000000 	 [ add ]:		 6546
Gene Set 	 130-elements 	 loop 80000000 	 [ add ]:		 7209

Enum Set 	 26-elements 	 loop 1000000 	 [ remove ]:		 48
Gene Set 	 26-elements 	 loop 1000000 	 [ remove ]:		 53

Enum Set 	 26-elements 	 loop 10000000 	 [ remove ]:		 464
Gene Set 	 26-elements 	 loop 10000000 	 [ remove ]:		 483

Enum Set 	 26-elements 	 loop 80000000 	 [ remove ]:		 3631
Gene Set 	 26-elements 	 loop 80000000 	 [ remove ]:		 3787

Enum Set 	 130-elements 	 loop 1000000 	 [ remove ]:		 82
Gene Set 	 130-elements 	 loop 1000000 	 [ remove ]:		 87

Enum Set 	 130-elements 	 loop 10000000 	 [ remove ]:		 817
Gene Set 	 130-elements 	 loop 10000000 	 [ remove ]:		 889

Enum Set 	 130-elements 	 loop 80000000 	 [ remove ]:		 6621
Gene Set 	 130-elements 	 loop 80000000 	 [ remove ]:		 7136
```

由于基于Mac个人机进行测试，存在一定的误差，基于80000000的Loop可以看到：

RegularEnumSet

- contains: 2.5%
- add: 14.3%
- remove: 4.3%

JumboEnumSet


- contains: 15.6%
- add: 10.1%
- remove: 7.8%

Random存在耗时，会导致性能更接近，因此，该提升数据存在一定的不精确性，但是可以明显的看到。无论是RegularEnumSet还是Jumbo，均存在明显的性能提升。