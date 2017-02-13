package eu.rampsoftware.er.domain.usecases;

import com.fernandocejas.arrow.checks.Preconditions;

import java.util.Currency;
import java.util.Date;
import java.util.List;

import eu.rampsoftware.er.data.CurrencyData;
import eu.rampsoftware.er.data.CurrencyRepository;
import eu.rampsoftware.er.data.PreferencesData;
import eu.rampsoftware.er.domain.QueryUseCase;
import eu.rampsoftware.er.domain.pojo.CurrencyHome;
import eu.rampsoftware.er.domain.pojo.mapper.CurrencyMapper;
import io.reactivex.Observable;
import io.reactivex.Scheduler;

import static com.fernandocejas.arrow.checks.Preconditions.checkNotNull;

/**
 * Created by Ramps on 2017-02-06.
 */

public class GetCurrenciesUseCase extends QueryUseCase<CurrencyData, GetCurrenciesUseCase.CurrenciesParam> {

    private final CurrencyRepository mRepository;
    private final PreferencesData mPreferences;

    public GetCurrenciesUseCase(final Scheduler workScheduler,
                                final Scheduler observeScheduler,
                                CurrencyRepository repository,
                                PreferencesData preferencesData) {
        super(workScheduler, observeScheduler);
        mRepository = repository;
        mPreferences = preferencesData;
    }

    @Override
    protected Observable<CurrencyData> buildUseCaseObservable(final CurrenciesParam params) {
        mPreferences.setCurrenciesExchangeDate(params.date);
        return mRepository.getCurrencies(params.date);
    }

    public static class CurrenciesParam {
        public CurrenciesParam(final Date date) {
            this.date = checkNotNull(date);
        }
        Date date;
    }
}
