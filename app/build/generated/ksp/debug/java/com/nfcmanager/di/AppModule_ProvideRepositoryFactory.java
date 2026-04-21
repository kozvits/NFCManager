package com.nfcmanager.di;

import com.google.gson.Gson;
import com.nfcmanager.data.db.NfcCardDao;
import com.nfcmanager.domain.repository.NfcCardRepository;
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
public final class AppModule_ProvideRepositoryFactory implements Factory<NfcCardRepository> {
  private final Provider<NfcCardDao> daoProvider;

  private final Provider<Gson> gsonProvider;

  public AppModule_ProvideRepositoryFactory(Provider<NfcCardDao> daoProvider,
      Provider<Gson> gsonProvider) {
    this.daoProvider = daoProvider;
    this.gsonProvider = gsonProvider;
  }

  @Override
  public NfcCardRepository get() {
    return provideRepository(daoProvider.get(), gsonProvider.get());
  }

  public static AppModule_ProvideRepositoryFactory create(Provider<NfcCardDao> daoProvider,
      Provider<Gson> gsonProvider) {
    return new AppModule_ProvideRepositoryFactory(daoProvider, gsonProvider);
  }

  public static NfcCardRepository provideRepository(NfcCardDao dao, Gson gson) {
    return Preconditions.checkNotNullFromProvides(AppModule.INSTANCE.provideRepository(dao, gson));
  }
}
