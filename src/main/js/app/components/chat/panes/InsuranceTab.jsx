import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import styled from 'styled-components';
import { Button, Header, Table } from 'semantic-ui-react';
import TableFields from 'components/shared/table-fields/TableFields';
import DateInput from 'components/shared/inputs/DateInput';

const DateCell = styled(Table.Cell)`
    &&& {
        display: flex;
        align-items: center;
    }
`;
export default class InsuranceTab extends React.Component {
    constructor(props) {
        super(props);
        this.state = { dateValue: null, datePickerDisabled: true };
    }

    dateChangeHandler = (type, e, { value }) => {
        this.setState({ dateValue: moment(value).format('YYYY-MM-DD') });
    };

    saveNewDate = () => {
        const { messages: { user }, saveInsuranceDate } = this.props;
        if (this.state.dateValue) {
            saveInsuranceDate(this.state.dateValue, user.hid);
            this.setState({ datePickerDisabled: true });
        }
    };

    toggleEdit = () => {
        this.setState({ datePickerDisabled: !this.state.datePickerDisabled });
    };

    downloadClick = () => {
        const { messages: { user } } = this.props;
        //eslint-disable-next-line
        window.open(`/api/member/mandate/${user.hid}`);
    };

    render() {
        const { insurance: { data } } = this.props;
        let activeDate;
        let fields;
        if (data) {
            activeDate = activeDate ? activeDate : data.insuranceActiveFrom;
            fields = { ...data };
            delete fields.insuranceActiveFrom;
        }

        return fields ? (
            <Table selectable>
                <Table.Body>
                    <Table.Row>
                        <Table.Cell>Insurance Mandate</Table.Cell>
                        <Table.Cell>
                            <Button onClick={this.downloadClick}>
                                Download
                            </Button>
                        </Table.Cell>
                    </Table.Row>
                    <TableFields fields={fields} />
                    <Table.Row>
                        <Table.Cell>Insurance active from</Table.Cell>
                        <DateCell>
                            {this.state.datePickerDisabled ? (
                                <React.Fragment>
                                    {activeDate}
                                    <Button onClick={this.toggleEdit}>
                                        Edit
                                    </Button>
                                </React.Fragment>
                            ) : (
                                <React.Fragment>
                                    <DateInput
                                        changeHandler={this.dateChangeHandler}
                                    />
                                    <Button onClick={this.saveNewDate} primary>
                                        Save
                                    </Button>
                                    <Button onClick={this.toggleEdit}>
                                        Close
                                    </Button>
                                </React.Fragment>
                            )}
                        </DateCell>
                    </Table.Row>
                </Table.Body>
            </Table>
        ) : (
            <Header>No insurance info </Header>
        );
    }
}

InsuranceTab.propTypes = {
    insurance: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    saveInsuranceDate: PropTypes.func.isRequired
};
