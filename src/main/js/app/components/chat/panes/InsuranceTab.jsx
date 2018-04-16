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

const CellButton = styled(Button)`
    &&& {
        margin: 0 8px;
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
        const { messages: { member }, saveInsuranceDate } = this.props;
        if (this.state.dateValue) {
            saveInsuranceDate(this.state.dateValue, member.hid);
            this.setState({ datePickerDisabled: true });
        }
    };

    toggleEdit = () => {
        this.setState({ datePickerDisabled: !this.state.datePickerDisabled });
    };

    downloadClick = () => {
        const { messages: { member } } = this.props;
        //eslint-disable-next-line
        window.open(`/api/member/mandate/${member.hid}`);
    };

    cancelInsuranceClick = () => {
        const { insurance: { data }, sendCancelRequest } = this.props;
        sendCancelRequest(data.memberId);
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
                    <Table.Row>
                        <Table.Cell>
                            Send cancellation email to existing insurer
                        </Table.Cell>
                        <Table.Cell>
                            <Button onClick={this.cancelInsuranceClick}>
                                Send
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
                                    <CellButton onClick={this.toggleEdit}>
                                        Edit
                                    </CellButton>
                                </React.Fragment>
                            ) : (
                                <React.Fragment>
                                    <DateInput
                                        changeHandler={this.dateChangeHandler}
                                    />
                                    <CellButton
                                        onClick={this.saveNewDate}
                                        primary
                                    >
                                        Save
                                    </CellButton>
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
    saveInsuranceDate: PropTypes.func.isRequired,
    sendCancelRequest: PropTypes.func.isRequired
};
