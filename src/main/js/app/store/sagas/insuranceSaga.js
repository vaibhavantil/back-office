import { call, put, takeLatest } from "redux-saga/effects";
import {
  INSURANCE_REQUESTING,
  SAVE_INSURANCE_DATE,
  SEND_CANCEL_REQUEST,
  SEND_CERTIFICATE,
  MEMBER_COMPANY_STATUS,
  INSURANCES_LIST_REQUESTING,
  MEMBER_CREATE_MODIFIED_INSURANCE,
  MODIFY_INSURANCE
} from "constants/members";
import api from "app/api";
import config from "app/api/config";
import {
  insuranceGetSuccess,
  insurancesListGetSuccess,
  insuranceGetError,
  saveActivationDateSuccess,
  saveCancellationDateSuccess,
  sendCancelRequestSuccess,
  sendCertificateSuccess,
  changeCompanyStatusSuccess,
  insuranceError,
  createModifiedInsuranceSuccess,
  modifyInsuranceSuccess
} from "../actions/insuranceActions";
import { showNotification } from "../actions/notificationsActions";
import { ACTIVATION_DATE, CANCELLATION_DATE } from "app/lib/messageTypes";

function* requestFlow({ id }) {
  try {
    const { data } = yield call(api, config.insurance.get, null, id);
    yield put(insuranceGetSuccess(data));
  } catch (error) {
    yield [put(insuranceGetError(error.message))];
  }
}

function* requestListofInsurancesFlow({ id }) {
  try {
    const requestListPath = `${id}/insurances`;

    const response = yield call(
      api,
      config.insurance.getInsurancesByMember,
      null,
      requestListPath
    );

    yield put(insurancesListGetSuccess(response.data));
  } catch (error) {
    yield [put(insuranceGetError(error.message))];
  }
}

function* createModifiedInsuranceFlow({ memberId, modifiedDetails }) {
  try {
    const path = `${memberId}/createmodifiedProduct`;

    const response = yield call(
      api,
      config.insurance.createModifiedInsurance,
      {
        idToBeReplaced: modifiedDetails.productId,
        memberId: memberId,
        street: modifiedDetails.street,
        city: modifiedDetails.city,
        zipCode: modifiedDetails.zipCode,
        floor: modifiedDetails.floor,
        livingSpace: modifiedDetails.livingSpace,
        houseType: modifiedDetails.insuranceType,
        personsInHouseHold: modifiedDetails.personsInHouseHold,
        safetyIncreasers: Array.isArray(modifiedDetails.safetyIncreasers)
          ? modifiedDetails.safetyIncreasers
          : modifiedDetails.safetyIncreasers.trim().split(","),
        isStudent: modifiedDetails.isStudent
      },
      path
    );

    yield put(createModifiedInsuranceSuccess(response.data));
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* modifyInsuranceFlow({ memberId, request }) {
  try {
    const path = `${memberId}/modifyProduct`;

    const response = yield call(
      api,
      config.insurance.modifyProduct,
      request,
      path
    );

    yield put(modifyInsuranceSuccess(response.data));
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* saveDateFlow({ id, date, changeType }) {
  try {
    switch (changeType) {
      case ACTIVATION_DATE:
        const activationPath = `${id}/activate`;
        yield call(
          api,
          config.insurance.setDate,
          { activationDate: date },
          activationPath
        );
        yield put(saveActivationDateSuccess(date));
        break;
      case CANCELLATION_DATE:
        const cancellationPath = `${id}/cancel`;
        yield call(
          api,
          config.insurance.setDate,
          { cancellationDate: date },
          cancellationPath
        );
        yield put(saveCancellationDateSuccess(date));
        break;
      default:
        throw "Function: saveDateFlow ErrorMessage: Not valid changeType";
    }
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* cancelRequestFlow({ id }) {
  try {
    const path = `${id}/sendCancellationEmail`;
    yield call(api, config.insurance.cancel, null, path);
    yield [
      put(sendCancelRequestSuccess()),
      put(
        showNotification({
          message: "Success",
          header: "Send cancellation email to existing insurer",
          type: "olive"
        })
      )
    ];
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* sendCertificateFlow({ data, memberId }) {
  try {
    const path = `${memberId}/certificate`;
    yield call(api, config.insurance.cert, data, path);
    yield [
      put(sendCertificateSuccess()),
      put(
        showNotification({
          message: "Success",
          header: "Upload insurance certificate",
          type: "olive"
        })
      )
    ];
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* changeCompanyStatusFlow({ value, memberId }) {
  try {
    const path = `${memberId}/insuredAtOtherCompany`;
    yield call(
      api,
      config.insurance.companyStatus,
      { insuredAtOtherCompany: value },
      path
    );
    yield [
      put(changeCompanyStatusSuccess(value)),
      put(
        showNotification({
          message: "Success",
          header: '"Insured at other company" field changed',
          type: "olive"
        })
      )
    ];
  } catch (error) {
    yield [
      put(
        showNotification({
          message: error.message,
          header: "Insurance"
        })
      ),
      put(insuranceError())
    ];
  }
}

function* insuranceWatcher() {
  yield [
    takeLatest(INSURANCE_REQUESTING, requestFlow),
    takeLatest(SAVE_INSURANCE_DATE, saveDateFlow),
    takeLatest(SEND_CANCEL_REQUEST, cancelRequestFlow),
    takeLatest(SEND_CERTIFICATE, sendCertificateFlow),
    takeLatest(MEMBER_COMPANY_STATUS, changeCompanyStatusFlow),
    takeLatest(INSURANCES_LIST_REQUESTING, requestListofInsurancesFlow),
    takeLatest(MEMBER_CREATE_MODIFIED_INSURANCE, createModifiedInsuranceFlow),
    takeLatest(MODIFY_INSURANCE, modifyInsuranceFlow)
  ];
}

export default insuranceWatcher;
