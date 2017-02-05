package eu.rampsoftware.er.domain;


import com.fernandocejas.arrow.checks.Preconditions;

import eu.rampsoftware.er.domain.executor.PostExecutionThread;
import eu.rampsoftware.er.domain.executor.ThreadExecutor;
import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public abstract class NoArgUseCase<R> {
    private final CompositeDisposable mDisposables;
    private final ThreadExecutor threadExecutor;
    private final PostExecutionThread postExecutionThread;

    NoArgUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread) {
        this.threadExecutor = threadExecutor;
        this.postExecutionThread = postExecutionThread;
        this.mDisposables = new CompositeDisposable();
    }

    public void run(DisposableObserver<R> observer) {
        Preconditions.checkNotNull(observer);
        final Observable<R> observable = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.from(threadExecutor))
                .observeOn(postExecutionThread.getScheduler());
        addDisposable(observable.subscribeWith(observer));
    }

    public void dispose() {
        if (!mDisposables.isDisposed()) {
            mDisposables.dispose();
        }
    }

    protected abstract Observable<R> buildUseCaseObservable();

    private void addDisposable(Disposable disposable) {
        mDisposables.add(disposable);
    }
}