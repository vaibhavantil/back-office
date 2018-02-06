import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import 'moment/locale/sv';
import { Form } from 'semantic-ui-react';
import styled from 'styled-components';
import 'react-dates/initialize';
import { SingleDatePicker } from 'react-dates';
import { OPEN_UP } from 'react-dates/constants';
import 'react-dates/lib/css/_datepicker';
import { DATE } from 'app/lib/messageTypes';
import TextInput from './TextInput';

const WidgetContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: flex-start;
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

export default class DateInput extends React.Component {
    constructor() {
        super();
        this.state = {
            focused: false
        };
    }

    dateChangeHandler = date => {
        this.setState({
            date: moment(date)
        });

        this.props.changeHandler(DATE, null, {
            value: moment(date).toISOString()
        });
    };

    componentDidMount() {
        if (!this.state.date && !this.props.date) {
            this.setState({ date: moment() }, () => {
                this.dateChangeHandler(this.state.date);
            });
        }
    }

    render() {
        const { changeHandler, cleanupForm } = this.props;
        return (
            <React.Fragment>
                <TextInput
                    changeHandler={changeHandler}
                    cleanupForm={cleanupForm}
                />
                <Form.Field>
                    <label>Date</label>
                    <WidgetContainer>
                        <DatePickerContainer>
                            <SingleDatePicker
                                date={this.state.date}
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
                </Form.Field>
            </React.Fragment>
        );
    }
}

DateInput.propTypes = {
    cleanupForm: PropTypes.bool,
    date: PropTypes.object,
    changeHandler: PropTypes.object.isRequired
};
