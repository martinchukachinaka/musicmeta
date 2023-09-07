package com.cc.ice.musicmeta.application.artists.service;

import com.cc.ice.musicmeta.application.artists.request.AliasAddRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasRemoveRequest;
import com.cc.ice.musicmeta.application.artists.request.AliasUpdateRequest;

public interface ArtistAliasMgr {

	void addAlias(AliasAddRequest request);

	void updateAlias(AliasUpdateRequest request);

	void removeAlias(AliasRemoveRequest request);
}