package org.echo.ddd.support.infrastructure.id;

import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdPrefixGeneratorNotFoundException;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * 单词分解器：由大写字母组成
 * <p>
 * 1、由一个单词构成的类，取前三个字母，若有冲突，则依次再取，直到不重复；
 * 2、由两个单词构成的类，取首单词两个字母，再取次单词首字母，若有冲突，则取次单词二字母，依此后推，直到不重复
 * 3、由三个以上单词构成的类，取前三个单词首字母，若有冲突，则取三单词二字母，依此后推，直到不重复
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j

public class WordsToUpperCaseString implements WordsToString {
    private static final String mark = "#";

    @Override
    public String toString(String[] words, int length, PrefixExists<Boolean, String> callback) {
        log.debug("Words to String ");
        if (words == null || words.length == 0 || length < 2)
            throw new IdPrefixGeneratorNotFoundException();

        String[] result = initResult(length);
        putLetterToResult(result, words, callback);
        return toString(result).toUpperCase();
    }

    private String[] initResult(int length) {
        String[] s = new String[length];
        for (int i = 0; i < length; i++) {
            s[i] = mark;
        }
        return s;
    }

    private boolean isFilled(String[] ss) {
        for (String s : ss) {
            if (s.equals(mark))
                return false;
        }
        return true;
    }

    private void putLetterToResult(String[] result, String[] words, PrefixExists<Boolean, String> call) {
        Deque<String> stringDeque = toDeque(words);
        int resultIndex = 0;
        int resultLength = result.length - 1;
        while(!isFilled(result)){
            if(!stringDeque.isEmpty() && resultIndex <= resultLength){
                result[resultIndex++] = stringDeque.pop();
            }else{
                break;
            }
        }

        int index = 1;
        while(!isFilled(result)){
            result[resultIndex++] = intToString(1);
            index = 2;
        }

        int indexReplace = resultIndex - 1;
        while(call.callback(toString(result))){
            if(!stringDeque.isEmpty()){
                result[resultLength] = stringDeque.pop();
            }else{
                if(index <= 9){
                    result[indexReplace] = intToString(index);
                }else{
                    index = 2;
                    indexReplace--;
                    result[indexReplace] = intToString(index);
                }
                index++;
            }
        }
    }

    private Deque<String> toDeque(String[] words){
        ArrayDeque<String> deque = new ArrayDeque<>();
        if(words.length == 1){
            addToDeque(deque,words[0]);
        }else{
            String lastWord = words[words.length - 1];
            for(String word:words){
                if (word.equals(lastWord)) {
                    addToDeque(deque,word);
                }else{
                    deque.add(getString(word, 0));
                }
            }
        }
        return deque;
    }

    private void addToDeque(Deque<String> deque,String word){
        char[] chars = word.toCharArray();
        for(char c:chars){
            deque.add(Character.toString(c));
        }
    }

    private String intToString(int index) {
        return Integer.toString(index);
    }

    private String getString(String s, int index) {
        char[] chars = s.toCharArray();
        return Character.toString(chars[index]);
    }

    private String toString(String[] ss){
        StringBuilder sb = new StringBuilder();
        for(String s:ss){
            sb.append(s);
        }
        return sb.toString();
    }
}