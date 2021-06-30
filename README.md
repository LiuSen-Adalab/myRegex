[![Build Status](https://travis-ci.com/LiuSen-Adalab/myRegex.svg?branch=master)](https://travis-ci.com/LiuSen-Adalab/myRegex)
# myRegex
本项目参考自 [regex](https://github.com/xindoo/regex) 。

这是一个简易的基于 NFA（非确定性有限状态机） 的正则表达式引擎，不能作为生产工具使用，可以作为了解正则表达式原理的练手项目
### 特性
- 支持 5 种正则特殊符号： "*"、"?"、"+"、"()"、"|"，以及英文字母和数字， 如正则表达式：" a(b|c)*" 将生成如下的状态机：
- 支持完全匹配判断和提取子串

        String pattern = "a(b|c)*";
        String test = "abbbccabc";
        Regex regex = Regex.compile(testPattern);
        boolean ismatch = regex.isMatch(test);      // false
        List<String> matchStrs = regex.match(test); // {"abbbcc", "abc"}
    