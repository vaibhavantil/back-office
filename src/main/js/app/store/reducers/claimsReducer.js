import initialState from "../initialState";
import {
  CLAIMS_REQUESTING,
  CLAIMS_REQUEST_SUCCESS,
  CLAIM_TYPES,
  CLAIM_TYPES_SUCCESS,
  CLAIMS_BY_MEMBER,
  CLAIMS_BY_MEMBER_SUCCESS,
  CLAIMS_ERROR,
  SORT_CLAIMS_LIST
} from "../constants/claims";
import { sortClaimsList } from "app/lib/helpers";

export default function(state = initialState.claims, action) {
  switch (action.type) {
    case CLAIM_TYPES:
    case CLAIMS_REQUESTING:
    case CLAIMS_BY_MEMBER:
      return {
        ...state,
        requesting: true
      };

    case CLAIMS_REQUEST_SUCCESS:
      return {
        ...state,
        list: sortClaimsList(
          action.claims,
          action.fieldName,
          action.isDescendingOrder
        ),
        requesting: false
      };

    case CLAIM_TYPES_SUCCESS:
      return {
        ...state,
        types: action.types.map((type, id) => ({
          ...type,
          key: id,
          value: type.name,
          text: type.title
        }))
      };

    case CLAIMS_BY_MEMBER_SUCCESS:
      return {
        ...state,
        memberClaims: action.claims,
        requesting: false
      };

    case CLAIMS_ERROR:
      return {
        ...state,
        requesting: false
      };
    case SORT_CLAIMS_LIST:
      return {
        ...state,
        list: sortClaimsList(
          [...state.list],
          action.fieldName,
          action.isReverse
        )
      };
    default:
      return state;
  }
}
