package org.echo.ddd.support.infrastructure.id;

import lombok.*;
import org.echo.ddd.domain.id.Identity;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
@ToString
public class IdPrefixTestStringIdentity implements Identity<String> {

    private String id;

}