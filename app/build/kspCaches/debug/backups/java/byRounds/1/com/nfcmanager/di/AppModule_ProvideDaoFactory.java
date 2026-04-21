package com.nfcmanager.di;

import com.nfcmanager.data.db.NFCDatabase;
import com.nfcmanager.data.db.NfcCardDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class AppModule_ProvideDaoFactory implements Factory<NfcCardDao> {
  private final Provider<NFCDatabase> dbProvider;

  public AppModule_ProvideDaoFactory(Provider<NFCDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public NfcCardDao get() {
    return provideDao(dbProvider.get());
  }

  public static AppModule_ProvideDaoFactory create(Provider<NFCDatabase> dbProvider) {
    return new AppModule_ProvideDaoFactory(dbProvider);
  }

  public static NfcCardDao provideDao(NFCDatabase db) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideDao(db));
  }
}
