package com.nfcmanager.data.repository;

import com.google.gson.Gson;
import com.nfcmanager.data.db.NfcCardDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class NfcCardRepositoryImpl_Factory implements Factory<NfcCardRepositoryImpl> {
  private final Provider<NfcCardDao> daoProvider;

  private final Provider<Gson> gsonProvider;

  public NfcCardRepositoryImpl_Factory(Provider<NfcCardDao> daoProvider,
      Provider<Gson> gsonProvider) {
    this.daoProvider = daoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public NfcCardRepositoryImpl get() {
    return newInstance(daoProvider.get(), gsonProvider.get());
  }

  public static NfcCardRepositoryImpl_Factory create(Provider<NfcCardDao> daoProvider,
      Provider<Gson> gsonProvider) {
    return new NfcCardRepositoryImpl_Factory(daoProvider, gsonProvider);
  }

  public static NfcCardRepositoryImpl newInstance(NfcCardDao dao, Gson gson) {
    return new NfcCardRepositoryImpl(dao, gson);
  }
}
