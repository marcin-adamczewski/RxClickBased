package com.appunite.rxclickbased;

import org.junit.Before;
import org.junit.Test;

import rx.observers.TestSubscriber;


public class PresenterTest {

    private Presenter presenter;

    @Before
    public void setUp() {
        presenter = new Presenter();
    }

    @Test
    public void testSampleOperator_whenTypedCredentialsAndClicked_emitsOnlyOnEvents() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsSampleObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("John Rambo");
    }

    @Test
    public void testSampleOperator_whenTypedCredentialsAndDoubleClicked_emitsOnlyOneEvent() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsSampleObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);
        presenter.getLoginClickSubject().onNext(null);

        testSubscriber.assertValueCount(1);
    }

    @Test
    public void testSwitchMapOperator_whenTypedCredentialsAndClicked_emitsOnlyOnEvent() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsSwitchMapObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("John Rambo");
    }

    @Test
    public void testSwitchMapOperator_whenTypedCredentialsAndClickedAndStartedTyping_emitsTwoEvents() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsSwitchMapObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);
        presenter.getPasswordSubject().onNext("C");

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("John Rambo", "John C");
    }

    @Test
    public void testSwitchMapOperator_whenClickedThenType_emitsEvent() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsSwitchMapObservable()
                .subscribe(testSubscriber);

        presenter.getLoginClickSubject().onNext(null);
        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        
        testSubscriber.assertValueCount(1);
        testSubscriber.assertValue("John Rambo");
    }

    @Test
    public void testWithLatestFromOperator_TypeAndClick_emitsOneEvent() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsWithLatestFromObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);

        testSubscriber.assertValueCount(1);
        testSubscriber.assertValues("John Rambo");
    }

    @Test
    public void testWithLatestFromOperator_SameCredentialsTwice_emitsTwoEvents() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsWithLatestFromObservable()
                .subscribe(testSubscriber);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);

        presenter.getEmailSubject().onNext("John");
        presenter.getPasswordSubject().onNext("Rambo");
        presenter.getLoginClickSubject().onNext(null);

        testSubscriber.assertValueCount(2);
        testSubscriber.assertValues("John Rambo", "John Rambo");
    }

    @Test
    public void testWithLatestFromOperator_clickThenType_emitsNotEvents() {
        final TestSubscriber<String> testSubscriber = new TestSubscriber<>();
        presenter.getCredentialsWithLatestFromObservable()
                .subscribe(testSubscriber);

        presenter.getLoginClickSubject().onNext(null);
        presenter.getEmailSubject().onNext("Holy");
        presenter.getPasswordSubject().onNext("Cow");

        testSubscriber.assertValueCount(0);
    }


}
