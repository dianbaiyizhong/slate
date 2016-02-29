package hhm.slate.db;

import java.util.List;

import android.content.Context;

public interface IDBControl {

	public void save(Object obj);

	public void update(Object obj);

	public void delete(int id);

	public List query();

	public List query(String id);

}
