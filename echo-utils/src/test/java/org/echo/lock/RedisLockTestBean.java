package org.echo.lock;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.assertj.core.util.Lists;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode
@ToString(of = {"id","name"})
public class RedisLockTestBean implements Serializable {
    private Long id;

    private String name;

    private Double score;

    private LocalDateTime fetchTime;

    private LocalDateTime submitTime;

    private String maker;

    private ArrayList<String> makers = Lists.newArrayList();

    public RedisLockTestBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void fetch(){
        this.fetchTime = LocalDateTime.now();
    }

    public void scored(String maker,Double score){
        this.maker = maker;
        if(this.score != null)
            this.score += score;
        else
            this.score = score;

        this.submitTime = LocalDateTime.now();
        this.makers.add(maker);
    }

    public boolean isUndo(){
        return this.maker == null;
    }
}