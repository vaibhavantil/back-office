import { createStore, combineReducers, applyMiddleware } from 'redux';
import createSagaMiddleware from 'redux-saga';
import rootSaga from './sagas';
import reducers from './reducers';
import { reducer as reduxFormReducer } from 'redux-form';

const rootReducer = combineReducers({
    ...reducers,
    form: reduxFormReducer,
});

const configureStore = () => {
    const sagaMiddleware = createSagaMiddleware();

    return {
        ...createStore(rootReducer, applyMiddleware(sagaMiddleware)),
        runSaga: sagaMiddleware.run(rootSaga)
    };
};

export default {
    configureStore
};