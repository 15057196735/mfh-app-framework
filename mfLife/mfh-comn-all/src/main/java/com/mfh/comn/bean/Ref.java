package com.mfh.comn.bean;

/**
 * 创建时间: 2011-5-1
 * 
 * @author zhangyz
 * @version 1.0
 * @see 本类功能:可用于java的输出参数,相当于输出参数包装器，
 * 使用泛型，支持任何类型，使用时需明确指定类型
 * 
 */
public class Ref<T> {
	private  T value;
	
	public Ref() {
		;
	}
	
	public Ref(T value) {
		this.value = value;
	}

	public String toString() {
		return value != null ? value.toString() : "";
	}

	public T getValue() {
		return value;
	}

	public void setValue(T value) {
		this.value = value;
	}
}

