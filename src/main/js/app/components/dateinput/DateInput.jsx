import React from 'react';
import moment from 'moment';
import 'moment/locale/sv';

import styled from 'styled-components';
import 'react-dates/initialize';
import { SingleDatePicker } from 'react-dates';
import { OPEN_UP } from 'react-dates/constants';
import 'react-dates/lib/css/_datepicker';

const WidgetContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    > * {
        margin-top: 10px;
        &:first-child {
            margin-top: 0;
        }
    }
`;

const DatePickerContainer = styled.div`
    font-family: 'Circular Std Book' !important;
    .SingleDatePickerInput {
        border: 1px solid #ccc;
        .DateInput {
            background-color: transparent;
            input {
                background-color: transparent;
                box-sizing: border-box !important;
                border: 24px;
                text-align: center;
                font-size: 16px;
                line-height: 24px;
                padding: 7px 20px;
            }
        }
    }
`;

/* eslint-disable react/prop-types */
class DateInput extends React.Component {
    constructor() {
        super();
        this.state = {
            focused: false
        };
        this.dateChangeHandler = this.dateChangeHandler.bind(this);
    }

    dateChangeHandler(date) {
        this.props.onChangeHandler(moment(date));
    }

    render() {
        return (
            <WidgetContainer>
                <DatePickerContainer>
                    <SingleDatePicker
                        onDateChange={this.dateChangeHandler}
                        focused={this.state.focused}
                        onFocusChange={({ focused }) =>
                            this.setState({ focused })
                        }
                        numberOfMonths={1}
                        isOutsideRange={() => false}
                        openDirection={OPEN_UP}
                        readOnly={true}
                        hideKeyboardShortcutsPanel={true}
                    />
                </DatePickerContainer>
            </WidgetContainer>
        );
    }
}

export default DateInput;
