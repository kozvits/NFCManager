package com.nfcmanager.domain.usecase;

import com.nfcmanager.domain.repository.NfcCardRepository;
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
public final class ClearSelectedCardUseCase_Factory implements Factory<ClearSelectedCardUseCase> {
  private final Provider<NfcCardRepository> repositoryProvider;

  public ClearSelectedCardUseCase_Factory(Provider<NfcCardRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public ClearSelectedCardUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static ClearSelectedCardUseCase_Factory create(
      Provider<NfcCardRepository> repositoryProvider) {
    return new ClearSelectedCardUseCase_Factory(repositoryProvider);
  }

  public static ClearSelectedCardUseCase newInstance(NfcCardRepository repository) {
    return new ClearSelectedCardUseCase(repository);
  }
}
