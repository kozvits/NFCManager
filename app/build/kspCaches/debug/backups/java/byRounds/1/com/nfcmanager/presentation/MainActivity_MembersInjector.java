package com.nfcmanager.presentation;

import com.nfcmanager.domain.repository.NfcCardRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<NfcCardRepository> repositoryProvider;

  public MainActivity_MembersInjector(Provider<NfcCardRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<NfcCardRepository> repositoryProvider) {
    return new MainActivity_MembersInjector(repositoryProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectRepository(instance, repositoryProvider.get());
  }

  @InjectedFieldSignature("com.nfcmanager.presentation.MainActivity.repository")
  public static void injectRepository(MainActivity instance, NfcCardRepository repository) {
    instance.repository = repository;
  }
}
