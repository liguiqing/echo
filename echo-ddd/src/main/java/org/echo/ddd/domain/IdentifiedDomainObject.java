/*
 * Copyright (c) 2016,2017, zhezhu All Rights Reserved. 深圳市天定康科技有限公司 版权所有.
 */

package org.echo.ddd.domain;

import org.echo.ddd.domain.id.Identity;

import java.io.Serializable;

/**
 * 具有唯一标识的领域对象
 *
 * @author Liguiqing
 * @since V1.0
 */
public interface IdentifiedDomainObject extends Serializable {
    Identity getId();
}