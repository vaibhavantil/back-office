import React from 'react';
import moment from 'moment';
import 'moment/locale/sv';
import { Input, Form } from 'semantic-ui-react';
import styled from 'styled-components';
import 'react-dates/initialize';
import { SingleDatePicker } from 'react-dates';
import { OPEN_UP } from 'react-dates/constants';
import 'react-dates/lib/css/_datepicker';
import { TEXT, DATE } from 'app/lib/messageTypes';

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

class DateInput extends React.Component {
    constructor() {
        super();
        this.state = {
            focused: false
        };
        this.dateChangeHandler = this.dateChangeHandler.bind(this);
    }

    dateChangeHandler(date) {
        this.setState({
            date: moment(date)
        });
        const dateArray = JSON.stringify([
            ...moment(date)
                .toArray()
                .slice(0, 3),
            0,
            0
        ]);
        this.props.changeHandler(DATE, null, { value: dateArray });
    }

    componentDidMount() {
        if (!this.state.date && !this.props.date) {
            this.setState({ date: moment() }, () => {
                this.dateChangeHandler(this.state.date);
            });
        }
    }

    render() {
        return (
            <React.Fragment>
                <Form.Field>
                    <label>Text</label>
                    <Input
                        fluid
                        onChange={this.props.changeHandler.bind(this, TEXT)}
                    />
                </Form.Field>
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

export default DateInput;
