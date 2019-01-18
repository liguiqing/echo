package org.echo.ddd.infrastructure.id;

import lombok.*;

import java.io.Serializable;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode(of = {"className"})
@ToString
public class IdPrefixBean implements Serializable {
    private String className;

    private String prefix;

    private Long idSeq;
}