import {
  INSURANCE_REQUESTING,
  INSURANCE_REQUEST_SUCCESS,
  INSURANCE_ERROR,
  SAVE_INSURANCE_DATE,
  SAVE_DATE_SUCCESS,
  SAVE_ACTIVATION_DATE_SUCCESS,
  SAVE_CANCELLATION_DATE_SUCCESS,
  SEND_CANCEL_REQUEST,
  SEND_CANCEL_REQUEST_SUCCESS,
  SEND_CERTIFICATE,
  SEND_CERTIFICATE_SUCCESS,
  MEMBER_COMPANY_STATUS,
  MEMBER_COMPANY_STATUS_SUCCESS,
  SEND_CERTIFICATE_ERROR
} from "../constants/members";

export const insuranceRequest = id => ({
  type: INSURANCE_REQUESTING,
  id
});

export const insuranceGetSuccess = insurance => ({
  type: INSURANCE_REQUEST_SUCCESS,
  insurance
});

export const insuranceGetError = error => ({
  type: INSURANCE_ERROR,
  error
});

export const saveInsuranceDate = (date, changeType, id) => ({
  type: SAVE_INSURANCE_DATE,
    date,
    changeType,
    id
});

export const saveActivationDateSuccess = activationDate => ({
    type: SAVE_ACTIVATION_DATE_SUCCESS,
    activationDate
});

export const saveCancellationDateSuccess = cancellationDate => ({
    type: SAVE_CANCELLATION_DATE_SUCCESS,
    cancellationDate
});

export const sendCancelRequest = id => ({
  type: SEND_CANCEL_REQUEST,
  id
});

export const sendCancelRequestSuccess = () => ({
  type: SEND_CANCEL_REQUEST_SUCCESS
});

export const sendCertificate = (data, memberId) => ({
  type: SEND_CERTIFICATE,
  data,
  memberId
});

export const sendCertificateSuccess = () => ({
  type: SEND_CERTIFICATE_SUCCESS
});

export const insuranceError = () => ({
  type: SEND_CERTIFICATE_ERROR
});

export const changeCompanyStatus = (value, memberId) => ({
  type: MEMBER_COMPANY_STATUS,
  value,
  memberId
});

export const changeCompanyStatusSuccess = value => ({
  type: MEMBER_COMPANY_STATUS_SUCCESS,
  value
});
