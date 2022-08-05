package dev.vality.scrooge.dao;

import dev.vality.scrooge.dao.domain.tables.pojos.Adapter;

import java.util.List;

public interface AdapterDao {

    Adapter save(Adapter adapter);

    List<Adapter> getAll();

    Adapter getByProviderId(Integer providerId);
}

