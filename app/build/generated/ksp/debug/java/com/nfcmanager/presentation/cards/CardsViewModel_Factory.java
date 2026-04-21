package com.nfcmanager.presentation.cards;

import com.nfcmanager.domain.usecase.ClearSelectedCardUseCase;
import com.nfcmanager.domain.usecase.DeleteCardUseCase;
import com.nfcmanager.domain.usecase.GetAllCardsUseCase;
import com.nfcmanager.domain.usecase.GetSelectedCardUseCase;
import com.nfcmanager.domain.usecase.SetSelectedCardUseCase;
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
public final class CardsViewModel_Factory implements Factory<CardsViewModel> {
  private final Provider<GetAllCardsUseCase> getAllCardsProvider;

  private final Provider<DeleteCardUseCase> deleteCardProvider;

  private final Provider<SetSelectedCardUseCase> setSelectedCardProvider;

  private final Provider<GetSelectedCardUseCase> getSelectedCardProvider;

  private final Provider<ClearSelectedCardUseCase> clearSelectedCardProvider;

  public CardsViewModel_Factory(Provider<GetAllCardsUseCase> getAllCardsProvider,
      Provider<DeleteCardUseCase> deleteCardProvider,
      Provider<SetSelectedCardUseCase> setSelectedCardProvider,
      Provider<GetSelectedCardUseCase> getSelectedCardProvider,
      Provider<ClearSelectedCardUseCase> clearSelectedCardProvider) {
    this.getAllCardsProvider = getAllCardsProvider;
    this.deleteCardProvider = deleteCardProvider;
    this.setSelectedCardProvider = setSelectedCardProvider;
    this.getSelectedCardProvider = getSelectedCardProvider;
    this.clearSelectedCardProvider = clearSelectedCardProvider;
  }

  @Override
  public CardsViewModel get() {
    return newInstance(getAllCardsProvider.get(), deleteCardProvider.get(), setSelectedCardProvider.get(), getSelectedCardProvider.get(), clearSelectedCardProvider.get());
  }

  public static CardsViewModel_Factory create(Provider<GetAllCardsUseCase> getAllCardsProvider,
      Provider<DeleteCardUseCase> deleteCardProvider,
      Provider<SetSelectedCardUseCase> setSelectedCardProvider,
      Provider<GetSelectedCardUseCase> getSelectedCardProvider,
      Provider<ClearSelectedCardUseCase> clearSelectedCardProvider) {
    return new CardsViewModel_Factory(getAllCardsProvider, deleteCardProvider, setSelectedCardProvider, getSelectedCardProvider, clearSelectedCardProvider);
  }

  public static CardsViewModel newInstance(GetAllCardsUseCase getAllCards,
      DeleteCardUseCase deleteCard, SetSelectedCardUseCase setSelectedCard,
      GetSelectedCardUseCase getSelectedCard, ClearSelectedCardUseCase clearSelectedCard) {
    return new CardsViewModel(getAllCards, deleteCard, setSelectedCard, getSelectedCard, clearSelectedCard);
  }
}
