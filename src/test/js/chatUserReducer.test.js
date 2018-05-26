import * as types from "../../main/js/app/store/constants/chatUsers";
import reducer from "../../main/js/app/store/reducers/chatUserReducer";

describe("Members (chats) reducer", () => {
  it("should return the initial state", () => {
    expect(reducer(undefined, {})).toEqual({
      list: [],
      requesting: false,
      successful: false,
      errors: [],
      filter: ""
    });
  });

  it("should handle fetch users", () => {
    const state = {
      list: [],
      requesting: false,
      successful: false,
      errors: [],
      filter: ""
    };
    const result = reducer(state, {
      type: types.USERS_REQUEST_SUCCESS,
      users: [
        {
          email: "string",
          memberId: 0,
          status: "SIGNED"
        }
      ]
    });
    expect(!!result.list.length).toEqual(true);
  });

  it("should handle filter change", () => {
    const state = {
      list: [],
      requesting: false,
      successful: false,
      errors: [],
      filter: ""
    };

    const result = reducer(state, {
      type: types.SET_USER_FILTER,
      query: {
        status: "INITIATED"
      }
    });

    expect(result.filter).toEqual("INITIATED");
  });
});
