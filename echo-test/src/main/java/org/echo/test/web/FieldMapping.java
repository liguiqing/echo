package org.echo.test.web;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liguiqing
 * @since V3.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FieldMapping {

    private String field;

    private Object object;
}