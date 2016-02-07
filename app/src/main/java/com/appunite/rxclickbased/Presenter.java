package com.appunite.rxclickbased;

import rx.Observable;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;

public class Presenter {

    private final Observable<String> credentialsSampleObservable;
    private final Observable<String> credentialsSwitchMapObservable;
    private final Observable<String> credentialsWithLatestFromObservable;

    private final BehaviorSubject<String> emailSubject = BehaviorSubject.create();
    private final BehaviorSubject<String> passwordSubject = BehaviorSubject.create();
    private final PublishSubject<Void> loginClickSubject = PublishSubject.create();

    public Presenter() {

        final Observable<String> lastCredentialsObservable = Observable.combineLatest(
                emailSubject,
                passwordSubject,
                new Func2<String, String, String>() {
                    @Override
                    public String call(String email, String password) {
                        return email + " " + password;
                    }
                });

        credentialsSampleObservable = lastCredentialsObservable
                .sample(loginClickSubject);

        credentialsSwitchMapObservable = loginClickSubject
                .switchMap(new Func1<Void, Observable<String>>() {
                    @Override
                    public Observable<String> call(Void ignore) {
                        return lastCredentialsObservable;
                    }
                });

        credentialsWithLatestFromObservable = loginClickSubject
                .withLatestFrom(lastCredentialsObservable, new Func2<Void, String, String>() {
                    @Override
                    public String call(Void ignore, String credentials) {
                        return credentials;
                    }
                });
    }

    public Observable<String> getCredentialsWithLatestFromObservable() {
        return credentialsWithLatestFromObservable;
    }

    public Observable<String> getCredentialsSwitchMapObservable() {
        return credentialsSwitchMapObservable;
    }

    public Observable<String> getCredentialsSampleObservable() {
        return credentialsSampleObservable;
    }

    public BehaviorSubject<String> getEmailSubject() {
        return emailSubject;
    }

    public BehaviorSubject<String> getPasswordSubject() {
        return passwordSubject;
    }

    public PublishSubject<Void> getLoginClickSubject() {
        return loginClickSubject;
    }
}
