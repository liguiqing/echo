package org.echo.ddd.support.infrastructure.id;

import lombok.AllArgsConstructor;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
public class OneWordToString implements WordsToString {

    private WordsToString nextWordsToString;

    @Override
    public String toString(String[] words, int length, PrefixExistCallback<Boolean,String> call) {
        if(!isMyFood(words)){
            return nextWordsToString.toString(words, length, call);
        }

        char[] prefix = new char[length];
        if(words.length == 1){
            int i=0;
            for(;i<prefix.length;i++){
                prefix[i] = getChar(words[0],i);
            }
            while(call.callback(String.copyValueOf(prefix))){
                if(i < words[0].length()){
                    prefix[prefix.length - 1] = getChar(words[0],i);
                }else{
                    prefix[prefix.length - 1] = (char)(i+'0');
                }
                i++;
            }
        }
        return String.copyValueOf(prefix);
    }

    protected char getChar(String s,int index){
        char[] chars = s.toCharArray();
        if(index < chars.length){
            return chars[index];
        }
        return chars[chars.length - 1];
    }

    protected boolean isMyFood(String[] words){
        return words.length == 1;
    }
}