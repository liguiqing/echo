package org.echo.ddd.infrastructure.id;

import lombok.extern.slf4j.Slf4j;
import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

/**
 * 前缀规则：由大写字母组成
 * <p>
 * 1、由一个单词构成的类，取前三个字母，若有冲突，则依次再取，直到不重复；
 * 2、由两个单词构成的类，取首单词两个字母，再取次单词首字母，若有冲突，则取次单词二字母，依此后推，直到不重复
 * 3、由三个以上单词构成的类，取前三个单词首字母，若有冲突，则取三单词二字母，依此后推，直到不重复
 * </p>
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class JdbcIdPrefix implements IdPrefix<Class<? extends Identity>> {

    private Cache existsPrefixes;

    private final int length = 3;

    private JdbcStringIdentityGenerator jdbcStringIdentityGenerator;

    private WordsToString wordsToString;

    public JdbcIdPrefix(CacheManager cacheManager, JdbcStringIdentityGenerator jdbcStringIdentityGenerator) {
        this.existsPrefixes = cacheManager.getCache("IdPrefix#-1#-1");
        this.jdbcStringIdentityGenerator = jdbcStringIdentityGenerator;
    }

    public void loadPrefixes(){
        this.jdbcStringIdentityGenerator.existsPrefixes().ifPresent(prefixes -> prefixes.forEach(idPrefixBean -> existsPrefixes.put(idPrefixBean.getPrefix(), idPrefixBean)));
    }

    @Override
    public String of(Class<? extends Identity> aClass) {
        return existsPrefixes.get(aClass.getName(),()->genPrefix(aClass));

    }

    protected String genPrefix(Class<? extends Identity> aClass){
        String className = aClass.getSimpleName();
        String[] words = className.substring(0,className.lastIndexOf("Id")).split("(?=[A-Z])");

        char[] prefix = new char[this.length];

        this.wordsToString.toString(words,this.length,(s)->exists(s));
//        if(words.length == 1){
//            int i=0;
//            for(;i<prefix.length;i++){
//                prefix[i] = getChar(words[0],i);
//            }
//            while(exists(prefix)){
//                if(i < words[0].length()){
//                    prefix[prefix.length - 1] = getChar(words[0],i);
//                }else{
//                    prefix[prefix.length - 1] = (char)(i+'0');
//                }
//                i++;
//            }
//        }else if(words.length == 2){
//            prefix[0] = getChar(words[0],0);
//            prefix[1] = getChar(words[1],0);
//            prefix[2] = getChar(words[1],1);
//            int i=2;
//            while(exists(prefix)){
//                if(i < words[1].length()){
//                    prefix[prefix.length - 1] = getChar(words[1],i);
//                }else{
//                    prefix[prefix.length - 1] = (char)(i+'0');
//                }
//                i++;
//            }
//        }else{
//
//        }

        String idPrefix = String.copyValueOf(prefix).toUpperCase();
        this.jdbcStringIdentityGenerator.newPrefix(aClass);
        return idPrefix;
    }

    private boolean exists(String prefix){
        return this.existsPrefixes.get(prefix) != null;
    }

    private char getChar(String s,int index){
        char[] chars = s.toCharArray();
        if(index < chars.length){
            return chars[index];
        }
        return chars[chars.length - 1];
    }
}