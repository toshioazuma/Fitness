package com.onrushers.data.db.schema;

import com.onrushers.data.db.entity.AuthSessionEntity;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {
		AuthSessionEntity.class
})
public class OnRushersRealmModule {

}
