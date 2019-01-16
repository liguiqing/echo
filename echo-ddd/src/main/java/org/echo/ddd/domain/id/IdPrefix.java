package org.echo.ddd.domain.id;

/**
 * ID前缀
 *
 * 前缀规则：由大写字母组成
 *
 * 1、由一个单词构成的类，取前三个字母，若有冲突，则依次再取，直到不重复；
 * 2、由两个单词构成的类，取首单词两个字母，再取次单词首字母，若有冲突，则取次单词二字母，依此后推，直到不重复
 * 3、由三个以上单词构成的类，取前三个单词首字母，若有冲突，则取三单词二字母，依此后推，直到不重复
 *
 * @author Liguiqing
 * @since V3.0
 */

public interface IdPrefix<V> {
    V contact(V v);
}