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
public final class SetSelectedCardUseCase_Factory implements Factory<SetSelectedCardUseCase> {
  private final Provider<NfcCardRepository> repositoryProvider;

  public SetSelectedCardUseCase_Factory(Provider<NfcCardRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public SetSelectedCardUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static SetSelectedCardUseCase_Factory create(
      Provider<NfcCardRepository> repositoryProvider) {
    return new SetSelectedCardUseCase_Factory(repositoryProvider);
  }

  public static SetSelectedCardUseCase newInstance(NfcCardRepository repository) {
    return new SetSelectedCardUseCase(repository);
  }
}
