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
public final class GetAllCardsUseCase_Factory implements Factory<GetAllCardsUseCase> {
  private final Provider<NfcCardRepository> repositoryProvider;

  public GetAllCardsUseCase_Factory(Provider<NfcCardRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public GetAllCardsUseCase get() {
    return newInstance(repositoryProvider.get());
  }

  public static GetAllCardsUseCase_Factory create(Provider<NfcCardRepository> repositoryProvider) {
    return new GetAllCardsUseCase_Factory(repositoryProvider);
  }

  public static GetAllCardsUseCase newInstance(NfcCardRepository repository) {
    return new GetAllCardsUseCase(repository);
  }
}
