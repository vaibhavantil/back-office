import styled from 'styled-components';

export const ListPage = styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;
    max-width: 500px;
    margin: 0 auto;
`;

export const PageContainer = styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 100%;
    background-color: #f5f5f5;
`;

export const ListContainer = styled.div`
    width: ${props => props.autoWidth || '500px'};
    border: solid 1px #dfe0e0;
    border-radius: 5px;
    padding: 20px 10px;
    margin-top: 30px;
`;