/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.echo.data.index;


import com.google.common.collect.Sets;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.echo.util.CollectionsUtil;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collector;

/**
 * <p>
 * 统计/分析组
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-03 15:08
 * @since V1.0.0
 **/
@Getter
@EqualsAndHashCode(of={"title"})
public class IndexGroup<T> implements Serializable {

    private String title;

    private Set<Index> indexes;

    private Set<IndexGroup> children;

    protected IndexGroup(){ }

    public IndexGroup(String title) {
        this.title = title;
    }

    public IndexGroup append(Index aResult) {
        if (Objects.isNull(this.indexes))
            this.indexes = Sets.newHashSet();
        this.indexes.add(aResult);
        return this;
    }

    public IndexGroup appendChild(IndexGroup child) {
        if(child == this)
            return this;

        if (Objects.isNull(this.children)) {
            this.children = Sets.newHashSet();
        }
        this.children.add(child);
        return this;
    }

    public void cal(T t) {
        if (CollectionsUtil.isNotNullAndNotEmpty(this.indexes)) {
            this.indexes.forEach(r -> r.cal(t));
        }

        if (CollectionsUtil.isNotNullAndNotEmpty(this.children)) {
            this.children.forEach(c -> c.cal(t));
        }
    }

    private IndexGroup combine(IndexGroup other){
        indexesCombined(this.indexes, other.indexes);
        childrenCombined(this.children, other.children);
        return this;
    }

    private void indexesCombined(Set<Index> targets,Set<Index> sources){
        int a = 0;
        for (Index ti : targets) {
            int b = 0;
            for (Index si : sources) {
                if(a == b){
                    ti.combine(si);
                    break;
                }
                b ++ ;
            }
            a ++;
        }
    }

    private void childrenCombined(Set<IndexGroup> targets,Set<IndexGroup> sources){
        if(CollectionsUtil.isNullOrEmpty(targets)){
            return;
        }

        int a = 0;
        for (IndexGroup ig : targets) {
            int b = 0;
            for (IndexGroup sg : sources) {
                if(a == b){
                    ig.combine(sg);
                    break;
                }
                b ++ ;
            }
            a ++;
        }
    }

    private IndexGroup<T> deepClone(){
        IndexGroup copy = new IndexGroup();
        copy.title = this.title;
        if(CollectionsUtil.isNotNullAndNotEmpty(this.indexes)){
            for(Index index:this.indexes){
                copy.append(index.deepClone());
            }
        }

        if(CollectionsUtil.isNotNullAndNotEmpty(this.children)){
            for(IndexGroup ig:this.children){
                copy.appendChild(ig.deepClone());
            }
        }

        return copy;
    }

    public Collector<T,IndexGroup,IndexGroup> toCollector(){
        return Collector.of(()->this.deepClone(),(a,t)->a.cal(t),(a1,a2)->a1.combine(a2), a->a);
    }
}
