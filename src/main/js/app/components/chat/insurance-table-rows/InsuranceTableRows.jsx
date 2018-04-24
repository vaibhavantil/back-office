import React from 'react';
import PropTypes from 'prop-types';
import moment from 'moment';
import styled from 'styled-components';
import { Button, Radio, Table } from 'semantic-ui-react';
import DateInput from 'components/shared/inputs/DateInput';

const DateCell = styled(Table.Cell)`
    &&& {
        display: flex;
        align-items: center;
    }
`;

const FileButton = styled.label`
    display: flex;
    align-items: center;
    justify-content: center;
    height: 36px;
    width: 100px;
    cursor: pointer;
    background-color: #e0e1e2;
    color: #00000099 !important;
    font-weight: 700;
    border-radius: 0.28571429rem;

    &:hover {
        background-color: #cacbcd;
        color: #000000cc !important;
    }
`;

const FlexCell = styled(Table.Cell)`
    display: flex;
    flex-direction: row;
    justify-content: flex-start;
    align-items: center;
`;

export default class InsuranceTableRows extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            cancelBtnIsVisible: true,
            datePickerDisabled: true,
            dateValue: null
        };
    }

    saveNewDate = () => {
        const {
            messages: { member },
            saveInsuranceDate
        } = this.props;
        if (this.state.dateValue) {
            saveInsuranceDate(this.state.dateValue, member.hid);
            this.setState({ datePickerDisabled: true });
        }
    };

    cancelInsuranceClick = () => {
        const {
            insurance: { data },
            sendCancelRequest
        } = this.props;
        sendCancelRequest(data.memberId);
        this.setState({ cancelBtnIsVisible: false });
    };

    dateChangeHandler = (type, e, { value }) => {
        this.setState({ dateValue: moment(value).format('YYYY-MM-DD') });
    };

    toggleEdit = () => {
        this.setState({ datePickerDisabled: !this.state.datePickerDisabled });
    };

    downloadClick = () => {
        const {
            messages: { member }
        } = this.props;
        //eslint-disable-next-line no-undef
        window.open(`/api/member/mandate/${member.hid}`);
    };

    changeHandler = e => {
        const {
            messages: { member },
            sendCertificate
        } = this.props;
        // eslint-disable-next-line no-undef
        const formData = new FormData();
        formData.set('file', e.target.files[0]);
        sendCertificate(formData, member.hid);
    };

    changeCompanyStatus = (e, { value }) => {
        const {
            messages: { member },
            changeCompanyStatus
        } = this.props;
        changeCompanyStatus(value, member.hid);
    };

    render() {
        const { fields, activeDate, insurance } = this.props;
        const { cancelBtnIsVisible } = this.state;
        const certIsExist = insurance.data.certificateUploaded;
        return (
            <React.Fragment>
                <Table.Row>
                    <Table.Cell>Insured at other company</Table.Cell>
                    <Table.Cell>
                        <Radio
                            toggle
                            value={insurance.insuredAtOtherCompany}
                            onChange={this.changeCompanyStatus}
                            disabled={insurance.requesting}
                        />
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Insurance Mandate</Table.Cell>
                    <Table.Cell>
                        <Button onClick={this.downloadClick}>Download</Button>
                    </Table.Cell>
                </Table.Row>
                <Table.Row>
                    <Table.Cell>Insurance certificate</Table.Cell>
                    <FlexCell>
                        <input
                            type="file"
                            name="certFile"
                            id="certFile"
                            multiple={false}
                            onChange={this.changeHandler}
                            style={{ display: 'none' }}
                            ref={input => {
                                this.fileInput = input;
                            }}
                        />
                        {certIsExist && (
                            <span style={{ marginRight: '10px' }}>
                                Certificate is already added
                            </span>
                        )}

                        <FileButton htmlFor="certFile">Choose file</FileButton>
                    </FlexCell>
                </Table.Row>
                {fields.cancellationEmailSent || !cancelBtnIsVisible ? null : (
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
                )}
                <Table.Row>
                    <Table.Cell>Insurance active from</Table.Cell>
                    <DateCell>
                        {this.state.datePickerDisabled ? (
                            <React.Fragment>
                                {activeDate && (
                                    <span style={{ marginRight: '10px' }}>
                                        {activeDate}
                                    </span>
                                )}

                                <Button onClick={this.toggleEdit}>Edit</Button>
                            </React.Fragment>
                        ) : (
                            <React.Fragment>
                                <DateInput
                                    changeHandler={this.dateChangeHandler}
                                />
                                <Button onClick={this.saveNewDate} primary>
                                    Save
                                </Button>
                                <Button onClick={this.toggleEdit}>Close</Button>
                            </React.Fragment>
                        )}
                    </DateCell>
                </Table.Row>
            </React.Fragment>
        );
    }
}

InsuranceTableRows.propTypes = {
    insurance: PropTypes.object.isRequired,
    messages: PropTypes.object.isRequired,
    saveInsuranceDate: PropTypes.func.isRequired,
    sendCancelRequest: PropTypes.func.isRequired,
    sendCertificate: PropTypes.func.isRequired,
    changeCompanyStatus: PropTypes.func.isRequired,
    fields: PropTypes.object.isRequired,
    activeDate: PropTypes.string
};
