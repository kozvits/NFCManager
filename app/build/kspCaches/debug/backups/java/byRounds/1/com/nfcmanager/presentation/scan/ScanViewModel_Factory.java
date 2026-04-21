package com.nfcmanager.presentation.scan;

import com.nfcmanager.domain.usecase.SaveCardUseCase;
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
public final class ScanViewModel_Factory implements Factory<ScanViewModel> {
  private final Provider<SaveCardUseCase> saveCardProvider;

  public ScanViewModel_Factory(Provider<SaveCardUseCase> saveCardProvider) {
    this.saveCardProvider = saveCardProvider;
  }

  @Override
  public ScanViewModel get() {
    return newInstance(saveCardProvider.get());
  }

  public static ScanViewModel_Factory create(Provider<SaveCardUseCase> saveCardProvider) {
    return new ScanViewModel_Factory(saveCardProvider);
  }

  public static ScanViewModel newInstance(SaveCardUseCase saveCard) {
    return new ScanViewModel(saveCard);
  }
}
