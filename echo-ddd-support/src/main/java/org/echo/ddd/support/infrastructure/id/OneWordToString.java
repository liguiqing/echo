package org.echo.ddd.support.infrastructure.id;

import lombok.AllArgsConstructor;
import org.echo.ddd.domain.id.IdPrefixGeneratorNotFoundException;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
public class OneWordToString implements WordsToString {

    private WordsToString nextWordsToString;

    @Override
    public String toString(String[] words, int length, PrefixExists<Boolean,String> call) {
        if(!isMyFood(words)){
            if(nextWordsToString != null) {
                return nextWordsToString.toString(words, length, call);
            }
            if(words.length > 0){
                throw new IdPrefixGeneratorNotFoundException(toString(words));
            }else{
                throw new IdPrefixGeneratorNotFoundException();
            }
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
                    prefix[prefix.length - 1] = getIndex(prefix,i);
                }
                i++;
            }
        }
        return String.copyValueOf(prefix).toUpperCase();
    }

    protected char getIndex(char[] chars,int index){
        return (char) (index - chars.length + 1 +'0');
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

    protected String toString(String[] words){
        StringBuffer s = new StringBuffer();
        for(int i = 0;i<words.length;i++){
            s.append(words[i]);
        }
        return s.toString();
    }
}