package org.springframework.test.context;

import org.springframework.util.ObjectUtils;

import java.io.Serializable;

/**
 * A key that a custom context loader can use to cache own data.
 * @author <a href="mailto:kristian@zeniorD0Tno">Kristian Rosenvold</a>
 * */
public class ContextCacheKey {
	private final String key;

	public ContextCacheKey(Serializable key) {
		this.key = ObjectUtils.nullSafeToString(key); // + contextLoader.getClass().getName();
	}

	@SuppressWarnings({"RedundantIfStatement"})
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		ContextCacheKey key1 = (ContextCacheKey) o;

		if (key != null ? !key.equals(key1.key) : key1.key != null) {
			return false;
		}

		return true;
	}

	@Override
	public int hashCode() {
		return key != null ? key.hashCode() : 0;
	}
}
