import React from "react";
import PropTypes from "prop-types";
import { Header, Table } from "semantic-ui-react";
import TableFields from "components/shared/table-fields/TableFields";
import InsuranceTableRows from "../insurance-table-rows/InsuranceTableRows";

const InsuranceTab = props => {
  const {
    insurance: { data }
  } = props;
  let activeDate;
  let cancellationDate;
  let fields;
  if (data) {
    activeDate = activeDate ? activeDate : data.insuranceActiveFrom;
    cancellationDate = cancellationDate
      ? cancellationDate
      : data.insuranceActiveTo;
    /* eslint-disable no-unused-vars */
    const {
      insuranceActiveFrom,
      insuranceActiveTo,
      insuredAtOtherCompany,
      cancellationEmailSent,
      certificateUploaded,
      certificateUrl,
      ...filteredFields
    } = data;
    /* eslint-enable */

    fields = filteredFields;
  }

  return fields ? (
    <Table selectable>
      <Table.Body>
        <TableFields fields={fields} />
        <InsuranceTableRows
          activeDate={activeDate}
          cancellationDate={cancellationDate}
          fields={fields}
          {...props}
        />
      </Table.Body>
    </Table>
  ) : (
    <Header>No insurance info </Header>
  );
};

InsuranceTab.propTypes = {
  insurance: PropTypes.object.isRequired,
  messages: PropTypes.object.isRequired,
  saveInsuranceDate: PropTypes.func.isRequired,
  sendCancelRequest: PropTypes.func.isRequired
};

export default InsuranceTab;
