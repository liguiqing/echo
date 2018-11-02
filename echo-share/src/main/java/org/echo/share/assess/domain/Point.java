package org.echo.share.assess.domain;

import lombok.*;
import java.io.Serializable;

/**
 * 图片中的位置
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class Point implements Serializable {
    private int x;

    private int y;

}